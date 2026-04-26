package com.example.jkr.elesafe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "reports")
public class DamageReport extends Report {

    private DamageType damageType;
    private String description;
    private double estimatedValueRs;
    private String imagePath;
    private ReportStatus status;

    public enum DamageType {
        CROP, PROPERTY, VEHICLE, HUMAN_INJURY
    }

    public enum ReportStatus {
        PENDING, VERIFIED, REJECTED, RESOLVED
    }
}