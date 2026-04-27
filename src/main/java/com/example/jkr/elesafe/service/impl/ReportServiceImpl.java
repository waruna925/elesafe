package com.example.jkr.elesafe.service.impl;

import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.SightingReport;
import com.example.jkr.elesafe.repo.ReportRepository;
import com.example.jkr.elesafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final MongoTemplate mongoTemplate;

    private String generateReportId() {
        Query query = new Query(Criteria.where("_id").is("reportId"));
        Update update = new Update().inc("seq", 1);
        FindAndModifyOptions options = FindAndModifyOptions.options().returnNew(true).upsert(true);
        Map result = mongoTemplate.findAndModify(query, update, options, Map.class, "counters");
        long seq = result != null ? ((Number) result.get("seq")).longValue() : 1;
        return String.format("R%03d", seq);
    }
    @Override
    public SightingReport submitSightingReport(SightingReportRequest request, String reporterEmail) {

        // 1. Map the DTO (what the mobile app sent) into the Model (what the database needs)
        SightingReport report = SightingReport.builder()
                .reportId(generateReportId())             // Auto-generate "R001"
                .reporterId(reporterEmail)                // Securely attach the user's email
                .district(request.getDistrict())          // Grab from DTO
                .village(request.getVillage())            // Grab from DTO
                .numberOfElephants(request.getNumberOfElephants()) // Grab from DTO
                .behavior(request.getBehavior())          // Grab from DTO
                .additionalNotes(request.getAdditionalNotes())     // Grab from DTO
                // Auto-fill the exact current time if the mobile app didn't send one
                .dateTime(request.getDateTime() != null ? request.getDateTime() : LocalDateTime.now())
                .build();
        return reportRepository.save(report);
    }
}