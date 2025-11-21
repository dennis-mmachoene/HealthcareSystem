package com.healthcare.config;

/**
 * JMS (Java Message Service) configuration constants.
 * Defines queue and topic names for asynchronous messaging.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
public final class JMSConfig {
    
    private JMSConfig() {
        throw new AssertionError("Cannot instantiate constants class");
    }
    
    // ===================================
    // Connection Factory
    // ===================================
    public static final String CONNECTION_FACTORY_JNDI = "jms/ConnectionFactory";
    
    // ===================================
    // Queue Names (Point-to-Point)
    // ===================================
    public static final String NOTIFICATION_QUEUE = "jms/NotificationQueue";
    public static final String NOTIFICATION_QUEUE_JNDI = "java:jboss/jms/queue/NotificationQueue";
    
    public static final String EMAIL_QUEUE = "jms/EmailQueue";
    public static final String EMAIL_QUEUE_JNDI = "java:jboss/jms/queue/EmailQueue";
    
    public static final String APPOINTMENT_REMINDER_QUEUE = "jms/AppointmentReminderQueue";
    public static final String APPOINTMENT_REMINDER_QUEUE_JNDI = "java:jboss/jms/queue/AppointmentReminderQueue";
    
    // ===================================
    // Topic Names (Publish-Subscribe)
    // ===================================
    public static final String SYSTEM_NOTIFICATION_TOPIC = "jms/SystemNotificationTopic";
    public static final String SYSTEM_NOTIFICATION_TOPIC_JNDI = "java:jboss/jms/topic/SystemNotificationTopic";
    
    public static final String ANALYTICS_TOPIC = "jms/AnalyticsTopic";
    public static final String ANALYTICS_TOPIC_JNDI = "java:jboss/jms/topic/AnalyticsTopic";
    
    // ===================================
    // Message Properties
    // ===================================
    public static final String PROPERTY_USER_ID = "userId";
    public static final String PROPERTY_NOTIFICATION_TYPE = "notificationType";
    public static final String PROPERTY_PRIORITY = "priority";
    public static final String PROPERTY_TIMESTAMP = "timestamp";
    public static final String PROPERTY_SENDER = "sender";
    
    // ===================================
    // Message Types
    // ===================================
    public static final String TYPE_APPOINTMENT_REMINDER = "APPOINTMENT_REMINDER";
    public static final String TYPE_APPOINTMENT_CONFIRMATION = "APPOINTMENT_CONFIRMATION";
    public static final String TYPE_APPOINTMENT_CANCELLATION = "APPOINTMENT_CANCELLATION";
    public static final String TYPE_PRESCRIPTION_READY = "PRESCRIPTION_READY";
    public static final String TYPE_TEST_RESULTS = "TEST_RESULTS_AVAILABLE";
    public static final String TYPE_SYSTEM_ALERT = "SYSTEM_ALERT";
    
    // ===================================
    // Priority Levels
    // ===================================
    public static final int PRIORITY_LOW = 1;
    public static final int PRIORITY_NORMAL = 4;
    public static final int PRIORITY_HIGH = 7;
    public static final int PRIORITY_URGENT = 9;
    
    // ===================================
    // Message Expiration (milliseconds)
    // ===================================
    public static final long DEFAULT_TIME_TO_LIVE = 86400000; // 24 hours
    public static final long URGENT_TIME_TO_LIVE = 3600000; // 1 hour
    
    // ===================================
    // Delivery Settings
    // ===================================
    public static final int DELIVERY_MODE_PERSISTENT = 2;
    public static final int DELIVERY_MODE_NON_PERSISTENT = 1;
    public static final boolean DEFAULT_PERSISTENT = true;
    
    // ===================================
    // Connection Pool Settings
    // ===================================
    public static final int MIN_POOL_SIZE = 1;
    public static final int MAX_POOL_SIZE = 10;
    public static final int POOL_TIMEOUT_MS = 30000; // 30 seconds
}