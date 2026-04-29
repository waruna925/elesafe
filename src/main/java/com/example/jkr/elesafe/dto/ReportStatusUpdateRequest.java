package com.example.jkr.elesafe.dto;

import com.example.jkr.elesafe.model.DamageReport;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusUpdateRequest {
    // Captures the new status (PENDING, VERIFIED, REJECTED, RESOLVED) [cite: 110, 142]
    private DamageReport.ReportStatus status;
}