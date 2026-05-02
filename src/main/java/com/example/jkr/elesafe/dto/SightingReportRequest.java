package com.example.jkr.elesafe.dto;

import com.example.jkr.elesafe.model.SightingReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SightingReportRequest {

    private String district;
    private String village;
    private Double latitude;
    private Double longitude;
    private int numberOfElephants;
    private SightingReport.ElephantBehavior behavior;
    private String additionalNotes;
    private LocalDateTime dateTime;

    // ✅ ADD THIS
    private String imagePath;
}