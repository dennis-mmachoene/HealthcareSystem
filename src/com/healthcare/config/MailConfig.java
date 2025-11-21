package com.healthcare.config;

/**
 * Email configuration for notification system.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public final class MailConfig {
    
    private MailConfig() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // ===================================
    // SMTP Server Settings
    // ===================================
    public static final String SMTP_HOST = System.getenv().getOrDefault("SMTP_HOST", "smtp.gmail.com");
    public static final int SMTP_PORT = Integer.parseInt(System.getenv().getOrDefault("SMTP_PORT", "587"));
    public static final String SMTP_USERNAME = System.getenv().getOrDefault("SMTP_USERNAME", "");
    public static final String SMTP_PASSWORD = System.getenv().getOrDefault("SMTP_PASSWORD", "");
    
    // ===================================
    // Email Addresses
    // ===================================
    public static final String FROM_EMAIL = System.getenv().getOrDefault("SMTP_FROM", "noreply@healthcare.com");
    public static final String FROM_NAME = "Smart Healthcare System";
    public static final String SUPPORT_EMAIL = "support@healthcare.com";
    public static final String ADMIN_EMAIL = "admin@healthcare.com";
    
    // ===================================
    // SMTP Security Settings
    // ===================================
    public static final boolean ENABLE_TLS = true;
    public static final boolean ENABLE_SSL = false;
    public static final boolean ENABLE_AUTH = true;
    public static final boolean ENABLE_START_TLS = true;
    
    // ===================================
    // Email Templates
    // ===================================
    public static final String TEMPLATE_APPOINTMENT_CONFIRMATION = "appointment_confirmation.html";
    public static final String TEMPLATE_APPOINTMENT_REMINDER = "appointment_reminder.html";
    public static final String TEMPLATE_APPOINTMENT_CANCELLATION = "appointment_cancellation.html";
    public static final String TEMPLATE_REGISTRATION_WELCOME = "registration_welcome.html";
    public static final String TEMPLATE_PASSWORD_RESET = "password_reset.html";
    public static final String TEMPLATE_DOCTOR_APPROVAL = "doctor_approval.html";
    public static final String TEMPLATE_PRESCRIPTION_READY = "prescription_ready.html";
    
    // ===================================
    // Email Subject Templates
    // ===================================
    public static final String SUBJECT_APPOINTMENT_CONFIRMATION = "Appointment Confirmed - ";
    public static final String SUBJECT_APPOINTMENT_REMINDER = "Reminder: Upcoming Appointment - ";
    public static final String SUBJECT_APPOINTMENT_CANCELLATION = "Appointment Cancelled - ";
    public static final String SUBJECT_REGISTRATION = "Welcome to Smart Healthcare System";
    public static final String SUBJECT_PASSWORD_RESET = "Password Reset Request";
    public static final String SUBJECT_DOCTOR_APPROVED = "Your Doctor Application Has Been Approved";
    public static final String SUBJECT_PRESCRIPTION_READY = "Your Prescription is Ready";
    
    // ===================================
    // Email Configuration
    // ===================================
    public static final String MAIL_TRANSPORT_PROTOCOL = "smtp";
    public static final String MAIL_SMTP_AUTH = "true";
    public static final String MAIL_SMTP_STARTTLS_ENABLE = "true";
    public static final int MAIL_SMTP_TIMEOUT = 5000; // milliseconds
    public static final int MAIL_SMTP_CONNECTION_TIMEOUT = 5000; // milliseconds
    
    // ===================================
    // Email Sending Options
    // ===================================
    public static final boolean ENABLE_EMAIL_SENDING = true;
    public static final int MAX_RETRY_ATTEMPTS = 3;
    public static final int RETRY_DELAY_MS = 1000;
}