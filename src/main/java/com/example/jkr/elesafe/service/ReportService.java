package com.example.jkr.elesafe.service;

import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.SightingReport;

public interface ReportService {

    // Feature 1: Submit a Sighting Report
    SightingReport submitSightingReport(SightingReportRequest request, String reporterEmail);

}