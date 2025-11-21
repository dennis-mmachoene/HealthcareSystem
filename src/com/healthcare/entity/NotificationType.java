package com.healthcare.entity;

/**
 * Enumeration representing notification types.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public enum NotificationType {
    
    APPOINTMENT_REMINDER("Appointment Reminder", "Reminder for upcoming appointment"),
    APPOINTMENT_CONFIRMATION("Appointment Confirmation", "Confirmation of scheduled appointment"),
    APPOINTMENT_CANCELLATION("Appointment Cancellation", "Notification of cancelled appointment"),
    PRESCRIPTION_READY("Prescription Ready", "Prescription is ready for pickup"),
    TEST_RESULTS_AVAILABLE("Test Results Available", "Test results are now available"),
    NEW_APPOINTMENT("New Appointment", "New appointment has been scheduled"),
    SYSTEM("System Notification", "General system notification"),
    ALERT("Alert", "Important alert notification");
    
    private final String displayName;
    private final String description;
    
    NotificationType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    public static NotificationType fromString(String value) {
        if (value == null) {
            return null;
        }
        
        for (NotificationType type : NotificationType.values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Unknown notification type: " + value);
    }
}