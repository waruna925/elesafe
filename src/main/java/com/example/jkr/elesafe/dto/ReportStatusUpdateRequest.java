package com.example.jkr.elesafe.dto;

import com.example.jkr.elesafe.model.ReportStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportStatusUpdateRequest {
    private ReportStatus status;
}