package com.mindbridge.server.service;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.model.Appointment;
import com.mindbridge.server.repository.AppointmentRepository;
import com.mindbridge.server.util.AppointmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    // 전부 조회
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 감정 기록 추가
    public AppointmentDTO addAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(savedAppointment);
    }

    // 감정 기록 조회 (개별)
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        return appointment != null ? appointmentMapper.toDTO(appointment) : null;
    }

    // 감정 기록 조회 (날짜)
    public List<AppointmentDTO> getAppointmentsByDate(Date date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        return appointments.stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }


    // 감정 기록 수정
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        if (appointmentRepository.existsById(id)) {
            Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
            appointment.setId(id);
            Appointment updatedAppointment = appointmentRepository.save(appointment);
            return appointmentMapper.toDTO(updatedAppointment);
        } else {
            return null;
        }
    }

    // 감정 기록 삭제
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        RecordService recordService = new RecordService();
        if (appointment != null) {
            appointmentRepository.deleteById(id);
            // 진료에 연결된 녹음 파일이 있다면 삭제
            if (appointment.getRecord() != null) {
                recordService.deleteRecord(appointment.getRecord().getId());
            }
        }
    }
//    public void deleteAppointment(Long id) {
//        appointmentRepository.deleteById(id);
//    }
}
