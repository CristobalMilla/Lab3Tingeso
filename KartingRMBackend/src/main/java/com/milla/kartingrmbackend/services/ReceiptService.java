package com.milla.kartingrmbackend.services;

import com.milla.kartingrmbackend.dto.RentPreviewDTO;
import com.milla.kartingrmbackend.entities.FeeTypeEntity;
import com.milla.kartingrmbackend.entities.FrequencyDiscountEntity;
import com.milla.kartingrmbackend.entities.ReceiptEntity;
import com.milla.kartingrmbackend.entities.RentEntity;
import com.milla.kartingrmbackend.repositories.FrequencyDiscountRepository;
import com.milla.kartingrmbackend.repositories.ReceiptRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReceiptService {
    private final ReceiptRepository receiptRepository;
    private final RentService rentService;
    private final FrequencyDiscountRepository frequencyDiscountRepository;
    private final BirthdayService birthdayService;
    private final HolidayService holidayService;
    private final FeeTypeService feeTypeService;
    private final PeopleDiscountService peopleDiscountService;

    public ReceiptService(ReceiptRepository receiptRepository, RentService rentService, FrequencyDiscountRepository frequencyDiscountRepository, BirthdayService birthdayService, HolidayService holidayService, FeeTypeService feeTypeService, PeopleDiscountService peopleDiscountService) {
        this.receiptRepository = receiptRepository;
        this.rentService = rentService;
        this.frequencyDiscountRepository = frequencyDiscountRepository;
        this.birthdayService = birthdayService;
        this.holidayService = holidayService;
        this.feeTypeService = feeTypeService;
        this.peopleDiscountService = peopleDiscountService;
    }

    //Getters
    public List<ReceiptEntity> getAll(){
        return receiptRepository.findAll();
    }
    public ReceiptEntity getReceiptById(int id){
        return receiptRepository.findById(id).orElse(null);
    }
    //Get de la lista de receipts de una sola renta por su id
    public List<ReceiptEntity> getReceiptsByRentId(int id){
        return receiptRepository.getReceiptsByRentId(id);
    }
    //Save
    public ReceiptEntity save(ReceiptEntity receiptEntity){
        return receiptRepository.save(receiptEntity);
    }
    //Update
    public ReceiptEntity update(ReceiptEntity receiptEntity){
        return receiptRepository.save(receiptEntity);
    }

    //Get para obtener la tarifa correspondiente al recibo, segun su renta
    public BigDecimal getFeePriceByReceiptId(int id){
        ReceiptEntity receipt = receiptRepository.findById(id).orElse(null);
        if(receipt == null){
            return null;
        }
        else {
            int rentId = receipt.getRentId();
            FeeTypeEntity feeType = rentService.getFeeTypeByRentId(rentId);
            return feeType.getPrice();
        }
    }
    //Get para obtener el people_discount price del recibo, segun su renta
    public BigDecimal getPeopleDiscountPriceByReceiptId(int id){
        ReceiptEntity receipt = receiptRepository.findById(id).orElse(null);
        if(receipt == null){
            return null;
        }
        else {
            int rentId = receipt.getRentId();
            return rentService.getPeopleDiscountByRentId(rentId).getDiscount();
        }
    }

    //Seccion para crear renta y recibo
    //Funciones principales para crear una renta y recibos segun datos iniciales mandados por frontend
    //Estos datos son una renta completa excepto por precio final, y una lista de sub_clientes como strings
    //Este calculo se realiza como transaccion atomica

    //Funcion que obtiene el valor del la tarifa base
    public BigDecimal calculateBaseTariff(int rentId) {
        return rentService.getFeeTypeByRentId(rentId).getPrice();
    }
    //Funcion que obtiene el descuento de gente segun renta
    public BigDecimal calculatePeopleDiscount(int rentId) {
        return rentService.getPeopleDiscountByRentId(rentId).getDiscount();
    }
    //Funcion que calcula el descuento especial
    //Se calculan y obtienen, si existen, descuento por frecuencia, cumpleaños (Se asume del cliente principal, se puede cambiar) y por festivo
    //Se utiliza el numero menor obtenido, osea, el mejor descuento entre los 3
    //Se utiliza LocalDate.now(), por lo que se obtiene la fecha de hoy desde la maquina. Esto puede generar discrepancias dependiendo de desde donde se este corriendo la aplicacion
    public BigDecimal calculateSpecialDiscount(int rentId) {
        RentEntity rent = rentService.getById(rentId);
        int peopleAmount = rent.getPeopleNumber();

        // Get frequency discount
        FrequencyDiscountEntity frequencyDiscount = frequencyDiscountRepository.findByClientFrequency(peopleAmount);
        BigDecimal fDiscount;
        if (frequencyDiscount == null) {
            fDiscount = BigDecimal.ONE;
        }
        else {
            fDiscount = frequencyDiscount.getDiscount();
        }

        // Check if it's the client's birthday and get birthday discount
        BigDecimal bDiscount = BigDecimal.ONE;
        boolean isItsBirthday = birthdayService.isItsBirthday(rent.getMainClient(), rent.getRentDate());

        if (isItsBirthday) {
            BigDecimal birthdayDiscount = birthdayService.findBirthdayDiscountByName(rent.getMainClient());
            if (birthdayDiscount != null) {
                bDiscount = birthdayDiscount;
            }
        }

        // Get holiday discount
        BigDecimal hDiscount = holidayService.findHolidayDiscountByDate(rent.getRentDate());

        // Return the minimum discount (maximum benefit for the client)
        return fDiscount.min(hDiscount).min(bDiscount);
    }
    //Funcion que calcula los distintos campos de un recibo creado
    //Se asume que descuento por cantidad de personas y el especial son multiplicativos
    private ReceiptEntity createFullReceipt(ReceiptEntity receipt) {
        int rentId = receipt.getRentId();
        //Calculo de los atributos por separados
        BigDecimal baseTariff = calculateBaseTariff(rentId);
        BigDecimal peopleDiscount = calculatePeopleDiscount(rentId);
        BigDecimal specialDiscount = calculateSpecialDiscount(rentId);
        BigDecimal aggregatedPrice = baseTariff
                .multiply(peopleDiscount)
                .multiply(specialDiscount);
        BigDecimal ivaPrice = aggregatedPrice.multiply(BigDecimal.valueOf(0.21));
        BigDecimal finalPrice = aggregatedPrice.add(ivaPrice);
        //Set de los atributos calculados
        receipt.setBaseTariff(baseTariff);
        receipt.setSizeDiscount(peopleDiscount);
        receipt.setSpecialDiscount(specialDiscount);
        receipt.setAggregatedPrice(aggregatedPrice);
        receipt.setIvaPrice(ivaPrice);
        receipt.setFinalPrice(finalPrice);
        //Return
        return receiptRepository.save(receipt);
    }
    //Funcion principal que, con una renta incompleta (sin precio final) y lista de clientes
    //Guarda, transaccional y atomicamente, una renta con sus respectivos recibos
    //Al ser transaccional y con muchos calculos, es posible la perdida de datos, tener en cuenta
    @Transactional
    public RentEntity saveRentWithReceipts(RentEntity rent, List<String> subClients) {
        // Valida datos de renta
        validateRentData(rent);
        // Guarda la renta incompleta
        RentEntity savedRent = rentService.save(rent);
        // Genera recibos para cada sub cliente y los guarda en la base de datos
        List<ReceiptEntity> receiptList = subClients.stream()
                .map(subClient -> {
                    //Para cada recibo, se crea uno vacio, se setea el id de renta, se setea el subcliente
                    //Y se calcula y guarda el recibo con estos valores
                    ReceiptEntity receipt = new ReceiptEntity();
                    receipt.setRentId(savedRent.getRentId());
                    receipt.setSubClientName(subClient);
                    return createFullReceipt(receipt); // Calculate and save receipt
                })
                .toList();
        //Calcula el precio total de la renta, segun la lista de recibos
        BigDecimal totalPrice = receiptList.stream()
                .map(ReceiptEntity::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        savedRent.setTotalPrice(totalPrice);
        //Se actualiza la renta con el precio total calculado
        return rentService.update(savedRent);
    }
    //Validar datos validos de renta, modificables a gusto
    private void validateRentData(RentEntity rent) {
        if (rent.getPeopleNumber() < 1 || rent.getPeopleNumber() > 15) {
            throw new IllegalArgumentException("People number must be between 1 and 15");
        }
        if (rent.getFeeTypeId() == 0 || rent.getMainClient() == null) {
            throw new IllegalArgumentException("Mandatory fields are missing");
        }
    }


    // Service methods
    public RentPreviewDTO calculateRentPreview(RentEntity rent, List<String> subClients) {
        // Validate input
        validateRentData(rent);

        // Set rent code if not provided
        if (rent.getRentCode() == null || rent.getRentCode().isEmpty()) {
            rent.setRentCode(generateRentCode(rent));
        }

        // Calculate receipts WITHOUT saving and WITHOUT setting rentId
        List<ReceiptEntity> calculatedReceipts = subClients.stream()
                .map(subClient -> {
                    ReceiptEntity receipt = new ReceiptEntity();
                    // Don't set rentId - will be set when saving
                    receipt.setSubClientName(subClient);
                    return calculateReceiptFields(receipt, rent); // Calculate all fields
                })
                .toList();

        // Calculate total price
        BigDecimal totalPrice = calculatedReceipts.stream()
                .map(ReceiptEntity::getFinalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        rent.setTotalPrice(totalPrice);

        return new RentPreviewDTO(rent, calculatedReceipts);
    }

    @Transactional
    public RentEntity saveRentFromPreview(RentPreviewDTO preview) {
        if (preview.getRent() == null || preview.getReceipts() == null || preview.getReceipts().isEmpty()) {
            throw new IllegalArgumentException("Invalid preview data");
        }

        // Save the rent (gets auto-generated rentId)
        RentEntity savedRent = rentService.save(preview.getRent());

        // Save receipts with the generated rentId
        preview.getReceipts().forEach(receipt -> {
            receipt.setRentId(savedRent.getRentId());
            receiptRepository.save(receipt);
        });

        return savedRent;
    }

    // Extract calculation logic to reusable method
    private ReceiptEntity calculateReceiptFields(ReceiptEntity receipt, RentEntity rent) {
        // Same calculation logic as before but don't save
        BigDecimal baseTariff = feeTypeService.getFeeTypeById(rent.getFeeTypeId()).getPrice();
        BigDecimal peopleDiscount = peopleDiscountService.findByPeopleAmount(rent.getPeopleNumber()).getDiscount();
        BigDecimal specialDiscount = calculateSpecialDiscountForRent(rent);

        BigDecimal aggregatedPrice = baseTariff.multiply(peopleDiscount).multiply(specialDiscount);
        BigDecimal ivaPrice = aggregatedPrice.multiply(BigDecimal.valueOf(0.19));
        BigDecimal finalPrice = aggregatedPrice.add(ivaPrice);

        receipt.setBaseTariff(baseTariff);
        receipt.setSizeDiscount(peopleDiscount);
        receipt.setSpecialDiscount(specialDiscount);
        receipt.setAggregatedPrice(aggregatedPrice);
        receipt.setIvaPrice(ivaPrice);
        receipt.setFinalPrice(finalPrice);

        return receipt; // Return calculated receipt (not saved)
    }

    private String generateRentCode(RentEntity rent) {
        if (rent.getMainClient() == null || rent.getRentDate() == null) {
            throw new IllegalArgumentException("Main client and rent date are required for code generation");
        }

        String clientName = rent.getMainClient().replaceAll("\\s+", "").toLowerCase();
        String dateStr = rent.getRentDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return clientName + dateStr;
    }

    // Add new method that accepts RentEntity directly
    public BigDecimal calculateSpecialDiscountForRent(RentEntity rent) {
        int peopleAmount = rent.getPeopleNumber();

        // Get frequency discount
        FrequencyDiscountEntity frequencyDiscount = frequencyDiscountRepository.findByClientFrequency(peopleAmount);
        BigDecimal fDiscount = (frequencyDiscount == null) ? BigDecimal.ONE : frequencyDiscount.getDiscount();

        // Check if it's the client's birthday
        BigDecimal bDiscount = BigDecimal.ONE;
        boolean isItsBirthday = birthdayService.isItsBirthday(rent.getMainClient(), rent.getRentDate());
        if (isItsBirthday) {
            BigDecimal birthdayDiscount = birthdayService.findBirthdayDiscountByName(rent.getMainClient());
            if (birthdayDiscount != null) {
                bDiscount = birthdayDiscount;
            }
        }

        // Get holiday discount
        BigDecimal hDiscount = holidayService.findHolidayDiscountByDate(rent.getRentDate());

        // Return the minimum discount (maximum benefit for the client)
        return fDiscount.min(hDiscount).min(bDiscount);
    }
}