package com.example.jkr.elesafe.model;

public enum ReportStatus {
    PENDING,
    IN_PROGRESS,
    RESOLVED,
    /** @deprecated kept for backward compatibility with existing records */
    VERIFIED,
    /** @deprecated kept for backward compatibility with existing records */
    REJECTED
}
