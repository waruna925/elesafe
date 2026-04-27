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
public class SightingReport extends Report {

    private int numberOfElephants;
    private ElephantBehavior behavior;
    private String additionalNotes;

    public enum ElephantBehavior {
        CALM, AGGRESSIVE, MOVING, FEEDING
    }
}