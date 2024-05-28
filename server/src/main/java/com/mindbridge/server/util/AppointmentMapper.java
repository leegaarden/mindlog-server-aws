package com.mindbridge.server.util;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.model.Appointment;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    // private final RecordMapper recordMapper;


//    public AppointmentMapper(RecordMapper recordMapper){
//        this.recordMapper = recordMapper;
//    }
    public AppointmentMapper() {}

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

//        if (appointment.getRecord() != null) {
//            appointmentDTO.setRecordDTO(recordMapper.toDTO(appointment.getRecord()));
//        }

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

//        if (appointmentDTO.getRecordDTO() != null) {
//            appointment.setRecord(recordMapper.toEntity(appointmentDTO.getRecordDTO()));
//        }


        return appointment;
    }
}
