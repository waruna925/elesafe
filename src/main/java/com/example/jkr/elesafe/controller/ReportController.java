package com.example.jkr.elesafe.controller;

import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.SightingReport;
import com.example.jkr.elesafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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

        // 1. @AuthenticationPrincipal safely extracts the logged-in user's details directly from their JWT token.
        // userDetails.getUsername() will grab their secure email/NIC.
        String secureReporterEmail = userDetails.getUsername();

        // 2. Hand the DTO and the secure email to the Service layer we just wrote
        SightingReport savedReport = reportService.submitSightingReport(request, secureReporterEmail);

        // 3. Return the saved database object back to the mobile app with a "201 Created" success status
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }
}