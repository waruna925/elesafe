package com.example.jkr.elesafe.dto;

import com.example.jkr.elesafe.model.DamageReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DamageReportRequest {

    private String district;
    private String village;
    private DamageReport.DamageType damageType;
    private String description;
    private String imagePath;
    private LocalDateTime dateTime;
}