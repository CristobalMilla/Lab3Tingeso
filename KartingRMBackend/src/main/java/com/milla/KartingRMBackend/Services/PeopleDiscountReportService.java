package com.milla.KartingRMBackend.Services;

import com.milla.KartingRMBackend.Entities.PeopleDiscountEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PeopleDiscountReportService {
    private final PeopleDiscountService peopleDiscountService;
    private final RentService rentService;

    public PeopleDiscountReportService(PeopleDiscountService peopleDiscountService, RentService rentService) {
        this.peopleDiscountService = peopleDiscountService;
        this.rentService = rentService;
    }

    private List<String> getMonthsBetween(String startMonth, String endMonth) {
        YearMonth start = YearMonth.parse(startMonth);
        YearMonth end = YearMonth.parse(endMonth);
        List<String> months = new ArrayList<>();

        while (!start.isAfter(end)) {
            months.add(start.toString());
            start = start.plusMonths(1);
        }
        return months;
    }
    private Map<String, Object> calculateFinalRow(List<Map<String, Object>> report, List<String> months) {
        Map<String, Object> finalRow = new LinkedHashMap<>();
        finalRow.put("Description", "Total General");

        BigDecimal grandTotal = BigDecimal.ZERO;

        for (String month : months) {
            BigDecimal monthTotal = BigDecimal.ZERO;

            for (Map<String, Object> row : report) {
                monthTotal = monthTotal.add((BigDecimal) row.get(month));
            }

            finalRow.put(month, monthTotal);
            grandTotal = grandTotal.add(monthTotal);
        }

        finalRow.put("Total", grandTotal);
        return finalRow;
    }
    public List<Map<String, Object>> generatePeopleDiscountReport(String startMonth, String endMonth) {
        // Fetch all people discounts
        List<PeopleDiscountEntity> peopleDiscounts = peopleDiscountService.getAll();
        if(peopleDiscounts == null){
            return new ArrayList<>();
        }
        // Prepare months list
        List<String> months = getMonthsBetween(startMonth, endMonth);

        // Initialize report
        List<Map<String, Object>> report = new ArrayList<>();

        // Process each people discount
        for (PeopleDiscountEntity discount : peopleDiscounts) {
            Map<String, Object> row = new LinkedHashMap<>();
            row.put("Numero de personas", discount.getMinPeople() + "-" + discount.getMaxPeople());

            BigDecimal rowTotal = BigDecimal.ZERO;

            // Aggregate data for each month
            for (String month : months) {
                BigDecimal monthTotal = rentService.calculateTotalPriceForMonthByPeopleDiscountId(month, discount.getPeopleDiscountId());

                if (monthTotal == null) {
                    monthTotal = BigDecimal.ZERO;
                }
                row.put(month, monthTotal);
                rowTotal = rowTotal.add(monthTotal);
            }

            row.put("Total", rowTotal);
            report.add(row);
        }

        // Add final row for month totals
        Map<String, Object> finalRow = calculateFinalRow(report, months);
        report.add(finalRow);

        return report;
    }
}
