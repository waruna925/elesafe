package com.example.jkr.elesafe.controller;

import com.example.jkr.elesafe.dto.DamageReportRequest;
import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.DamageReport;
import com.example.jkr.elesafe.model.Report;
import com.example.jkr.elesafe.model.SightingReport;
import com.example.jkr.elesafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * FEATURE 1: Submit a Sighting Report
     * Endpoint: POST /api/reports/sighting
     */
    @PostMapping("/sighting")
    public ResponseEntity<SightingReport> createSightingReport(
            @RequestBody SightingReportRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String secureReporterEmail = userDetails.getUsername();
        SightingReport savedReport = reportService.submitSightingReport(request, secureReporterEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    /**
     * FEATURE 2: Submit a Damage Report
     * Endpoint: POST /api/reports/damage
     */
    @PostMapping("/damage")
    public ResponseEntity<DamageReport> createDamageReport(
            @RequestBody DamageReportRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String secureReporterEmail = userDetails.getUsername();
        DamageReport savedReport = reportService.submitDamageReport(request, secureReporterEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    /**
     * FEATURE 3: Get My Reports
     * Endpoint: GET /api/reports/my-reports

    @GetMapping("/my-reports")
    public ResponseEntity<List<Report>> getMyReports(
            @AuthenticationPrincipal UserDetails userDetails) {

        String secureReporterEmail = userDetails.getUsername();
        List<Report> myReports = reportService.getMyReports(secureReporterEmail);
        return ResponseEntity.ok(myReports);
    }
     */
}