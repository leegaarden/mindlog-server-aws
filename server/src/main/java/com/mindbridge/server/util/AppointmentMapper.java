package com.mindbridge.server.util;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    // Appointment -> AppointmentDTO
    public AppointmentDTO toDTO(Appointment appointment) {

        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDate(appointment.getDate());
        appointmentDTO.setStartTime(appointment.getStartTime());
        appointmentDTO.setEndTime(appointment.getEndTime());
        appointmentDTO.setDoctorName(appointment.getDoctorName());
        appointmentDTO.setHospital(appointment.getHospital());
        appointmentDTO.setMemo((appointment.getMemo()));

        return appointmentDTO;
    }

    // AppointmentDTO -> Appointment
    public Appointment toEntity(AppointmentDTO appointmentDTO) {

        Appointment appointment = new Appointment();

        appointment.setId(appointmentDTO.getId());
        appointment.setDate(appointmentDTO.getDate());
        appointment.setStartTime(appointmentDTO.getStartTime());
        appointment.setEndTime(appointmentDTO.getEndTime());
        appointment.setDoctorName(appointmentDTO.getDoctorName());
        appointment.setHospital(appointmentDTO.getHospital());
        appointment.setMemo((appointmentDTO.getMemo()));

        return appointment;
    }
}
