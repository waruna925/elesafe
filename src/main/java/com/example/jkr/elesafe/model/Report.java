package com.example.jkr.elesafe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "reports")
public abstract class Report {

    @Id
    private String reportId;
    private String reporterId;
    private String district;
    private String village;
    private LocalDateTime dateTime;
}