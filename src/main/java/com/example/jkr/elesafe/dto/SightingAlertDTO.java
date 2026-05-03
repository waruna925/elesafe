package com.example.jkr.elesafe.dto;

import com.example.jkr.elesafe.model.SightingReport;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class SightingAlertDTO {
    private String reportId;
    private String reporterId;       // email of reporter
    private String district;
    private String village;
    private Double latitude;
    private Double longitude;
    private int numberOfElephants;
    private SightingReport.ElephantBehavior behavior;
    private String additionalNotes;
    private LocalDateTime dateTime;
}