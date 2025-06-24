package com.milla.KartingRMBackend.Services;

import com.milla.KartingRMBackend.Entities.FeeTypeEntity;
import com.milla.KartingRMBackend.Repositories.FeeTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class FeeTypeReportService {

    @Autowired
    private FeeTypeService feeTypeService;
    @Autowired
    private RentService rentService;

    //Primera funcion que obtiene una lista de meses entre 2 meses ingresados
    //Funciona con años
    private List<String> getMonthsBetween(String startMonth, String endMonth) {
        // Example implementation to get all months between two dates
        YearMonth start = YearMonth.parse(startMonth);
        YearMonth end = YearMonth.parse(endMonth);
        List<String> months = new ArrayList<>();

        while (!start.isAfter(end)) {
            months.add(start.toString());
            start = start.plusMonths(1);
        }
        return months;
    }
    //Funcion que calcula la suma por mes y la suma total final
    private Map<String, Object> calculateFinalRow(List<Map<String, Object>> report, List<String> months) {
        Map<String, Object> finalRow = new LinkedHashMap<>();
        finalRow.put("Descripcion", "Total General");

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (String month : months) {
            BigDecimal monthTotal = BigDecimal.ZERO;

            for (Map<String, Object> row : report) {
                monthTotal = monthTotal.add((BigDecimal) row.get(month));
            }

            finalRow.put(month, monthTotal);
            grandTotal = grandTotal.add(monthTotal);
        }

        finalRow.put("Gran Total", grandTotal);
        return finalRow;
    }
    //Funcion que, utilizando las anteriores, genera el la tabla reporte segun los tipos de tarifas
    public List<Map<String, Object>> generateFeeTypeReport(String startMonth, String endMonth) {
        // Fetch all fee types
        List<FeeTypeEntity> feeTypes = feeTypeService.getAll();

        // Prepare month list
        List<String> months = getMonthsBetween(startMonth, endMonth);

        // Initialize report
        List<Map<String, Object>> report = new ArrayList<>();

        // Process each fee type
        for (FeeTypeEntity feeType : feeTypes) {
            Map<String, Object> row = new LinkedHashMap<>();
            //Se extrae del fee_type su numero de vueltas y tiempo maximo, colocando en la primera celda de la fila
            row.put("Descripcion", String.format("Numero de vueltas: %d, Tiempo maximo permitido: %s",
                    feeType.getLapNumber(), feeType.getMaxTime()));

            BigDecimal rowTotal = BigDecimal.ZERO;
            //Para cada mes en la lista de meses, extraer:
            // Aggregate data for each month
            for (String month : months) {
                //Utilizando restTemplate, extraer todos los precios_finales de todas las rentas que se hayan hecho en ese mes, ya sumadas
                BigDecimal monthTotal = rentService.calculateTotalPriceForMonthByFeeTypeId(month, feeType.getFeeTypeId());

                if (monthTotal == null) {
                    monthTotal = BigDecimal.ZERO;
                }
                row.put(month, monthTotal);
                rowTotal = rowTotal.add(monthTotal);
            }
            //RowTotal siendo el total de cada fee_type
            row.put("Total", rowTotal);
            report.add(row);
        }
        //Finalmente, añadir la ultima fila de totales por mes, y total final
        // Add final row for month totals
        Map<String, Object> finalRow = calculateFinalRow(report, months);
        report.add(finalRow);

        return report;
    }




}
