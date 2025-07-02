package com.milla.kartingrmbackend.controllers;

import com.milla.kartingrmbackend.services.FeeTypeReportService;
import com.milla.kartingrmbackend.services.PeopleDiscountReportService;
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
    private final FeeTypeReportService feeTypeReportService;
    private final PeopleDiscountReportService peopleDiscountReportService;

    public ReportController(FeeTypeReportService feeTypeReportService, PeopleDiscountReportService peopleDiscountReportService) {
        this.feeTypeReportService = feeTypeReportService;
        this.peopleDiscountReportService = peopleDiscountReportService;
    }

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
