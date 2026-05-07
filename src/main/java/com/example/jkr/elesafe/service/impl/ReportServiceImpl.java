package com.example.jkr.elesafe.service.impl;

import com.example.jkr.elesafe.dto.DamageReportRequest;
import com.example.jkr.elesafe.dto.SightingAlertDTO;
import com.example.jkr.elesafe.dto.SightingReportRequest;
import com.example.jkr.elesafe.model.DamageReport;
import com.example.jkr.elesafe.model.Report;
import com.example.jkr.elesafe.model.SightingReport;
import com.example.jkr.elesafe.repo.ReportRepository;
import com.example.jkr.elesafe.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final MongoTemplate mongoTemplate;
    private final SimpMessagingTemplate messagingTemplate;

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
        SightingReport report = SightingReport.builder()
                .reportId(generateReportId())
                .reporterId(reporterEmail)
                .district(request.getDistrict())
                .village(request.getVillage())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .numberOfElephants(request.getNumberOfElephants())
                .behavior(request.getBehavior())
                .additionalNotes(request.getAdditionalNotes())
                // ✅ ADD THIS
                .imagePath(request.getImagePath())
                .dateTime(request.getDateTime() != null ? request.getDateTime() : LocalDateTime.now())
                .build();

        SightingReport saved = reportRepository.save(report);

        SightingAlertDTO alert = SightingAlertDTO.builder()
                .reportId(saved.getReportId())
                .reporterId(saved.getReporterId())
                .district(saved.getDistrict())
                .village(saved.getVillage())
                .latitude(saved.getLatitude())
                .longitude(saved.getLongitude())
                .numberOfElephants(saved.getNumberOfElephants())
                .behavior(saved.getBehavior())
                .additionalNotes(saved.getAdditionalNotes())
                .dateTime(saved.getDateTime())
                .imagePath(saved.getImagePath())
                .build();

        messagingTemplate.convertAndSend("/topic/alerts", alert);
        return saved;
    }

    @Override
    public DamageReport submitDamageReport(DamageReportRequest request, String reporterEmail) {
        DamageReport report = DamageReport.builder()
                .reportId(generateReportId())
                .reporterId(reporterEmail)
                .district(request.getDistrict())
                .village(request.getVillage())
                .damageType(request.getDamageType())
                .description(request.getDescription())
                .imagePath(request.getImagePath())
                .dateTime(request.getDateTime() != null ? request.getDateTime() : LocalDateTime.now())
                .status(DamageReport.ReportStatus.PENDING)
                .build();
        return reportRepository.save(report);
    }

    @Override
    public List<Report> getMyReports(String reporterEmail) {
        return reportRepository.findByReporterId(reporterEmail);
    }

    @Override
    public void deleteMyReport(String reportId, String reporterEmail) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found!"));

        if (!report.getReporterId().equals(reporterEmail)) {
            throw new RuntimeException("Security Alert: Unauthorized deletion attempt!");
        }
        reportRepository.deleteById(reportId);
    }

    @Override
    public DamageReport updateDamageReportStatus(String reportId, DamageReport.ReportStatus newStatus) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found!"));

        if (report instanceof DamageReport damageReport) {
            damageReport.setStatus(newStatus);
            return reportRepository.save(damageReport);
        } else {
            throw new RuntimeException("Only Damage Reports have a verifiable status!");
        }
    }

    @Override
    public List<Report> getReportsByVillage(String village) {
        return reportRepository.findByVillage(village);
    }

    @Override
    public List<Report> getRecentReports() {
        return reportRepository.findAllByOrderByDateTimeDesc();
    }

    @Override
    public Report getReportById(String reportId) {
        return reportRepository.findById(reportId)
                .orElseThrow(() -> new RuntimeException("Report not found with ID: " + reportId));
    }
}