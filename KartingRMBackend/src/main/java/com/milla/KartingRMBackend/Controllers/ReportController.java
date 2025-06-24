package com.milla.KartingRMBackend.Controllers;

import com.milla.KartingRMBackend.Services.FeeTypeReportService;
import com.milla.KartingRMBackend.Services.PeopleDiscountReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private FeeTypeReportService feeTypeReportService;

    @Autowired
    private PeopleDiscountReportService peopleDiscountReportService;

    @GetMapping("/fee-type")
    public ResponseEntity<List<Map<String, Object>>> generateFeeTypeReport(
            @RequestParam String startMonth,
            @RequestParam String endMonth) {
        return ResponseEntity.ok(feeTypeReportService.generateFeeTypeReport(startMonth, endMonth));
    }

    @GetMapping("/people-discount")
    public ResponseEntity<List<Map<String, Object>>> generatePeopleDiscountReport(
            @RequestParam String startMonth,
            @RequestParam String endMonth) {
        return ResponseEntity.ok(peopleDiscountReportService.generatePeopleDiscountReport(startMonth, endMonth));
    }
}
