package com.example.jkr.elesafe.controller;

import com.example.jkr.elesafe.dto.DamageReportRequest;
import com.example.jkr.elesafe.dto.ReportStatusUpdateRequest;
import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.DamageReport;
import com.example.jkr.elesafe.model.Report;
import com.example.jkr.elesafe.model.SightingReport;
import com.example.jkr.elesafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/sighting")
    public ResponseEntity<SightingReport> createSightingReport(
            @RequestBody SightingReportRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String secureReporterEmail = userDetails.getUsername();
        SightingReport savedReport = reportService.submitSightingReport(request, secureReporterEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    @PostMapping("/damage")
    public ResponseEntity<DamageReport> createDamageReport(
            @RequestBody DamageReportRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        String secureReporterEmail = userDetails.getUsername();
        DamageReport savedReport = reportService.submitDamageReport(request, secureReporterEmail);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedReport);
    }

    @GetMapping("/my-reports")
    public ResponseEntity<List<Report>> getMyReports(
            @AuthenticationPrincipal UserDetails userDetails) {
        String secureReporterEmail = userDetails.getUsername();
        List<Report> myReports = reportService.getMyReports(secureReporterEmail);
        return ResponseEntity.ok(myReports);
    }

    @GetMapping("/recent")
    public ResponseEntity<List<Report>> getRecentReports() {
        return ResponseEntity.ok(reportService.getRecentReports());
    }

    @PreAuthorize("hasAnyRole('WILD_OFFICER', 'ADMIN')")
    @GetMapping("/village/{village}")
    public ResponseEntity<List<Report>> getReportsByVillage(@PathVariable String village) {
        return ResponseEntity.ok(reportService.getReportsByVillage(village));
    }

    @GetMapping("/{reportId}")
    public ResponseEntity<Report> getReportById(@PathVariable String reportId) {
        return ResponseEntity.ok(reportService.getReportById(reportId));
    }

    @DeleteMapping("/{reportId}")
    public ResponseEntity<String> deleteMyReport(
            @PathVariable String reportId,
            @AuthenticationPrincipal UserDetails userDetails) {
        String secureReporterEmail = userDetails.getUsername();
        reportService.deleteMyReport(reportId, secureReporterEmail);
        return ResponseEntity.ok("Report " + reportId + " deleted successfully.");
    }

    @PreAuthorize("hasRole('WILD_OFFICER')")
    @PatchMapping("/damage/{reportId}/status")
    public ResponseEntity<DamageReport> updateDamageStatus(
            @PathVariable String reportId,
            @RequestBody ReportStatusUpdateRequest request) {
        DamageReport updatedReport = reportService.updateDamageReportStatus(reportId, request.getStatus());
        return ResponseEntity.ok(updatedReport);
    }
}