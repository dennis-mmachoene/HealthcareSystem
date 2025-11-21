package com.healthcare.service;

import com.healthcare.entity.*;
import com.healthcare.exception.NotFoundException;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Logger;

/**
 * Service class for Notification-related business operations.
 * Handles sending and managing user notifications.
 * 
 * @author Healthcare System Team
 * @version 1.0
 */
@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class NotificationService {
    
    private static final Logger LOGGER = Logger.getLogger(NotificationService.class.getName());
    
    @PersistenceContext(unitName = "HealthcarePU")
    private EntityManager entityManager;
    
    /**
     * Find notification by ID
     */
    public Notification findById(Long id) {
        LOGGER.info("Finding notification by ID: " + id);
        Notification notification = entityManager.find(Notification.class, id);
        if (notification == null) {
            throw new NotFoundException("Notification not found with ID: " + id);
        }
        return notification;
    }
    
    /**
     * Find notifications by user
     */
    public List<Notification> findByUser(Long userId) {
        LOGGER.info("Finding notifications for user ID: " + userId);
        TypedQuery<Notification> query = entityManager.createNamedQuery(
            "Notification.findByUser", Notification.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    /**
     * Find unread notifications for user
     */
    public List<Notification> findUnreadByUser(Long userId) {
        LOGGER.info("Finding unread notifications for user ID: " + userId);
        TypedQuery<Notification> query = entityManager.createNamedQuery(
            "Notification.findUnread", Notification.class);
        query.setParameter("userId", userId);
        return query.getResultList();
    }
    
    /**
     * Count unread notifications for user
     */
    public Long countUnread(Long userId) {
        TypedQuery<Long> query = entityManager.createNamedQuery(
            "Notification.countUnread", Long.class);
        query.setParameter("userId", userId);
        return query.getSingleResult();
    }
    
    /**
     * Create notification
     */
    public Notification createNotification(User user, NotificationType type, 
                                          String title, String message) {
        LOGGER.info("Creating notification for user: " + user.getEmail());
        
        Notification notification = new Notification(user, type, title, message);
        entityManager.persist(notification);
        entityManager.flush();
        
        LOGGER.info("Notification created successfully");
        return notification;
    }
    
    /**
     * Mark notification as read
     */
    public void markAsRead(Long notificationId) {
        LOGGER.info("Marking notification as read: " + notificationId);
        
        Notification notification = findById(notificationId);
        notification.markAsRead();
        entityManager.merge(notification);
        
        LOGGER.info("Notification marked as read");
    }
    
    /**
     * Mark all notifications as read for user
     */
    public void markAllAsRead(Long userId) {
        LOGGER.info("Marking all notifications as read for user ID: " + userId);
        
        List<Notification> unreadNotifications = findUnreadByUser(userId);
        for (Notification notification : unreadNotifications) {
            notification.markAsRead();
            entityManager.merge(notification);
        }
        
        LOGGER.info("All notifications marked as read");
    }
    
    /**
     * Delete notification
     */
    public void deleteNotification(Long id) {
        LOGGER.info("Deleting notification ID: " + id);
        
        Notification notification = findById(id);
        entityManager.remove(notification);
        
        LOGGER.info("Notification deleted successfully");
    }
    
    /**
     * Send appointment notification
     */
    public void sendAppointmentNotification(Appointment appointment) {
        LOGGER.info("Sending appointment notification");
        
        // Notify doctor
        String doctorMessage = String.format(
            "New appointment scheduled with patient %s on %s at %s",
            appointment.getPatient().getUser().getFullName(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime()
        );
        
        createNotification(
            appointment.getDoctor().getUser(),
            NotificationType.NEW_APPOINTMENT,
            "New Appointment",
            doctorMessage
        );
        
        // Notify patient
        String patientMessage = String.format(
            "Your appointment with Dr. %s has been scheduled for %s at %s",
            appointment.getDoctor().getUser().getFullName(),
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime()
        );
        
        createNotification(
            appointment.getPatient().getUser(),
            NotificationType.APPOINTMENT_CONFIRMATION,
            "Appointment Confirmed",
            patientMessage
        );
    }
    
    /**
     * Send appointment cancellation notification
     */
    public void sendAppointmentCancellationNotification(Appointment appointment) {
        LOGGER.info("Sending appointment cancellation notification");
        
        String message = String.format(
            "Your appointment on %s at %s has been cancelled. Reason: %s",
            appointment.getAppointmentDate(),
            appointment.getAppointmentTime(),
            appointment.getCancellationReason()
        );
        
        // Notify patient
        createNotification(
            appointment.getPatient().getUser(),
            NotificationType.APPOINTMENT_CANCELLATION,
            "Appointment Cancelled",
            message
        );
        
        // Notify doctor
        createNotification(
            appointment.getDoctor().getUser(),
            NotificationType.APPOINTMENT_CANCELLATION,
            "Appointment Cancelled",
            message
        );
    }
    
    /**
     * Send appointment reminder
     */
    public void sendAppointmentReminder(Appointment appointment) {
        LOGGER.info("Sending appointment reminder");
        
        String message = String.format(
            "Reminder: You have an appointment tomorrow with Dr. %s at %s",
            appointment.getDoctor().getUser().getFullName(),
            appointment.getAppointmentTime()
        );
        
        createNotification(
            appointment.getPatient().getUser(),
            NotificationType.APPOINTMENT_REMINDER,
            "Appointment Reminder",
            message
        );
    }
    
    /**
     * Send prescription notification
     */
    public void sendPrescriptionNotification(Prescription prescription) {
        LOGGER.info("Sending prescription notification");
        
        String message = String.format(
            "Dr. %s has prescribed %s. Please check your prescriptions.",
            prescription.getDoctor().getUser().getFullName(),
            prescription.getMedicationName()
        );
        
        createNotification(
            prescription.getPatient().getUser(),
            NotificationType.PRESCRIPTION_READY,
            "New Prescription",
            message
        );
    }
    
    /**
     * Send doctor approval notification
     */
    public void sendDoctorApprovalNotification(Doctor doctor) {
        LOGGER.info("Sending doctor approval notification");
        
        String message = "Congratulations! Your doctor application has been approved. " +
                        "You can now log in and start managing appointments.";
        
        createNotification(
            doctor.getUser(),
            NotificationType.SYSTEM,
            "Application Approved",
            message
        );
    }
    
    /**
     * Send system notification to all users of a role
     */
    public void sendSystemNotification(UserRole role, String title, String message) {
        LOGGER.info("Sending system notification to all " + role + " users");
        
        TypedQuery<User> query = entityManager.createNamedQuery("User.findByRole", User.class);
        query.setParameter("role", role);
        List<User> users = query.getResultList();
        
        for (User user : users) {
            createNotification(user, NotificationType.SYSTEM, title, message);
        }
        
        LOGGER.info("System notification sent to " + users.size() + " users");
    }
}