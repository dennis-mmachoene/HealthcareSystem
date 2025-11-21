package com.healthcare.entity;

/**
 * Enumeration representing approval status for doctor applications.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public enum ApprovalStatus {
    
    PENDING("Pending", "Awaiting admin approval"),
    APPROVED("Approved", "Application approved by admin"),
    REJECTED("Rejected", "Application rejected by admin");
    
    private final String displayName;
    private final String description;
    
    ApprovalStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static ApprovalStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("Unknown approval status: " + value);
    }
}