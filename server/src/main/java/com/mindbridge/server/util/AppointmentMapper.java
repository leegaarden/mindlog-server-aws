package com.mindbridge.server.util;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Appointment;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.service.RecordService;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    MindlogMapper mindlogMapper = new MindlogMapper();
    RecordMapper recordMapper = new RecordMapper();
    RecordService recordService = new RecordService();


    // Appointment -> AppointmentDTO
    public AppointmentDTO toDTO(Appointment appointment) {

        AppointmentDTO appointmentDTO = new AppointmentDTO();

        appointmentDTO.setId(appointment.getId());
        appointmentDTO.setDate(appointment.getDate());
        appointmentDTO.setStartTime(appointment.getStartTime());
        appointmentDTO.setEndTime(appointment.getEndTime());
        appointmentDTO.setDoctorName(appointment.getDoctorName());
        appointmentDTO.setHospital(appointment.getHospital());
        appointmentDTO.setMemo(appointment.getMemo());

        if (appointment.getMindlogs() != null) {
            for (Mindlog mindlog : appointment.getMindlogs()) {
                appointmentDTO.getMindlogDTOs().add(mindlogMapper.toDTO(mindlog));
            }
        }


        if (appointment.getRecord() != null) {
            appointmentDTO.setRecordId(appointment.getRecord().getId());
        }

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

        if (appointmentDTO.getMindlogDTOs() != null) {
            for (MindlogDTO mindlogDTO : appointmentDTO.getMindlogDTOs()) {
                appointment.getMindlogs().add(mindlogMapper.toEntity(mindlogDTO));
            }
        }

        if (appointmentDTO.getRecordId() != null) {
            appointment.setRecord(recordMapper.toEntity(recordService.getRecordById(appointmentDTO.getRecordId())));
        }


        return appointment;
    }
}
