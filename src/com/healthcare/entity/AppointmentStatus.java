package com.healthcare.entity;

/**
 * Enumeration representing appointment status types.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public enum AppointmentStatus {
    
    SCHEDULED("Scheduled", "Appointment has been scheduled"),
    CONFIRMED("Confirmed", "Appointment confirmed by both parties"),
    COMPLETED("Completed", "Appointment has been completed"),
    CANCELLED("Cancelled", "Appointment was cancelled"),
    NO_SHOW("No Show", "Patient did not attend the appointment");
    
    private final String displayName;
    private final String description;
    
    AppointmentStatus(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static AppointmentStatus fromString(String value) {
        if (value == null) {
            return null;
        }
        
        for (AppointmentStatus status : AppointmentStatus.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("Unknown appointment status: " + value);
    }
}