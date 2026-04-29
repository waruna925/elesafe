package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.DamageReportRequest;
import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.DamageReport;
import com.example.jkr.elesafe.model.SightingReport;

public interface ReportService {

    // Feature 1
    SightingReport submitSightingReport(SightingReportRequest request, String reporterEmail);

    // Feature 2: Submit a Damage Report
    DamageReport submitDamageReport(DamageReportRequest request, String reporterEmail);

}