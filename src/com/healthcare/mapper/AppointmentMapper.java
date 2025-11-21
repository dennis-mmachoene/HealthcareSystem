package com.healthcare.mapper;

import com.healthcare.dto.AppointmentDTO;
import com.healthcare.entity.Appointment;
import jakarta.enterprise.context.ApplicationScoped;

/**
 * Mapper for converting between Appointment entity and AppointmentDTO.
 * 
 * @author Dennis Mmachoene Ramara
 * @version 1.0
 */
@ApplicationScoped
public class AppointmentMapper {
    
    /**
     * Convert Appointment entity to AppointmentDTO
     */
    public AppointmentDTO toDTO(Appointment appointment) {
        if (appointment == null) {
            return null;
        }
        
        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
            if (appointment.getPatient().getUser() != null) {
                dto.setPatientName(appointment.getPatient().getUser().getFullName());
            }
        }
        
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
            if (appointment.getDoctor().getUser() != null) {
                dto.setDoctorName(appointment.getDoctor().getUser().getFullName());
            }
            dto.setDoctorSpecialization(appointment.getDoctor().getSpecialization());
        }
        
        dto.setAppointmentDate(appointment.getAppointmentDate());
        dto.setAppointmentTime(appointment.getAppointmentTime());
        dto.setDurationMinutes(appointment.getDurationMinutes());
        dto.setStatus(appointment.getStatus());
        dto.setReason(appointment.getReason());
        dto.setNotes(appointment.getNotes());
        
        return dto;
    }
    
    /**
     * Convert AppointmentDTO to Appointment entity
     */
    public Appointment toEntity(AppointmentDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Appointment appointment = new Appointment();
        appointment.setId(dto.getId());
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDurationMinutes(dto.getDurationMinutes());
        appointment.setStatus(dto.getStatus());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
        
        return appointment;
    }
    
    /**
     * Update existing Appointment entity with data from AppointmentDTO
     */
    public void updateEntity(Appointment appointment, AppointmentDTO dto) {
        if (appointment == null || dto == null) {
            return;
        }
        
        appointment.setAppointmentDate(dto.getAppointmentDate());
        appointment.setAppointmentTime(dto.getAppointmentTime());
        appointment.setDurationMinutes(dto.getDurationMinutes());
        appointment.setStatus(dto.getStatus());
        appointment.setReason(dto.getReason());
        appointment.setNotes(dto.getNotes());
    }
}