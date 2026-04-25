package com.example.jkr.elesafe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@SuperBuilder                        // ✅ inherits User builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true) // ✅ include parent fields in equals/hashCode
@Document(collection = "users")      // ✅ same collection as User
public class WildOfficer extends User {

    // ✅ Extra fields only for Wild Officers
    private String badgeNumber;
    private String station;
}