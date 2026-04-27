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

    // Location
    private String district;
    private String village;
    // Sighting Details
    private int numberOfElephants;
    private SightingReport.ElephantBehavior behavior;
    private String additionalNotes;
    // Optional: The app can send a specific time, or the server will auto-fill it
    private LocalDateTime dateTime;
}