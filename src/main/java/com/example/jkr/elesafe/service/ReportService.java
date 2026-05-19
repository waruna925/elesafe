package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.DamageReportRequest;
import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.DamageReport;
import com.example.jkr.elesafe.model.Report;
import com.example.jkr.elesafe.model.SightingReport;

import java.util.List;

public interface ReportService {
    SightingReport submitSightingReport(SightingReportRequest request, String reporterEmail);
    DamageReport submitDamageReport(DamageReportRequest request, String reporterEmail);
    List<Report> getMyReports(String reporterEmail);
    void deleteMyReport(String reportId, String reporterEmail);
    DamageReport updateDamageReportStatus(String reportId, DamageReport.ReportStatus newStatus);
    List<Report> getReportsByVillage(String village);
    List<Report> getRecentReports();
    Report getReportById(String reportId);

    /**
     * Returns all reports whose district matches the authenticated officer's
     * registered district. Throws AccessDeniedException if the officer's
     * profile has no district set.
     */
    List<Report> getReportsByOfficerDistrict(String officerEmail);

    /**
     * Returns reports for a specific district only if the requesting officer's
     * registered district matches the requested district.
     * Throws AccessDeniedException on mismatch.
     */
    List<Report> getReportsByDistrict(String requestedDistrict, String officerEmail);
}