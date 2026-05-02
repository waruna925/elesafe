package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.DamageReportRequest;
import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.DamageReport;
import com.example.jkr.elesafe.model.Report;
import com.example.jkr.elesafe.model.SightingReport;
import java.util.List;

public interface ReportService {
    SightingReport submitSightingReport(SightingReportRequest request, String reporterEmail) ;
    DamageReport submitDamageReport(DamageReportRequest request, String reporterEmail);
    List<Report> getMyReports(String reporterEmail);
    void deleteMyReport(String reportId, String reporterEmail);
    DamageReport updateDamageReportStatus(String reportId, DamageReport.ReportStatus newStatus);
    List<Report> getReportsByVillage(String village);
    List<Report> getRecentReports();

}