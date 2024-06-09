package com.mindbridge.server.service;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.dto.RecordDTO;
import com.mindbridge.server.exception.ResourceNotFoundException;
import com.mindbridge.server.model.Appointment;
import com.mindbridge.server.model.Record;
import com.mindbridge.server.repository.AppointmentRepository;
import com.mindbridge.server.repository.MindlogRepository;
import com.mindbridge.server.repository.RecordRepository;
import com.mindbridge.server.util.AppointmentMapper;
import com.mindbridge.server.util.MindlogMapper;
import com.mindbridge.server.util.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentMapper appointmentMapper;

    @Autowired
    private RecordMapper recordMapper;

    @Autowired
    private RecordRepository recordRepository;


    @Autowired
    private MindlogService mindlogService;
    // 전부 조회
    public List<AppointmentDTO> getAllAppointments() {
        List<Appointment> appointments = appointmentRepository.findAll();
        return appointments.stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 진료 추가
    public AppointmentDTO addAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDTO(savedAppointment);
    }

    // 진료 조회 (개별)
    public AppointmentDTO getAppointmentById(Long id) {
        Appointment appointment = appointmentRepository.findById(id).orElse(null);
        return appointment != null ? appointmentMapper.toDTO(appointment) : null;
    }

    // 진료 조회 (날짜)
    public List<AppointmentDTO> getAppointmentsByDate(Date date) {
        List<Appointment> appointments = appointmentRepository.findByDate(date);
        return appointments.stream()
                .map(appointmentMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 진료 수정
    public AppointmentDTO updateAppointment(Long id, AppointmentDTO appointmentDTO) {
        if (appointmentRepository.existsById(id)) {
            Appointment appointment1 = appointmentRepository.findById(id).orElse(null);
            AppointmentDTO appointmentDTO1 = appointmentMapper.toDTO(appointment1);
            Appointment appointment = appointmentMapper.toEntity(appointmentDTO);
            appointment.setId(id);

            Appointment updatedAppointment = appointmentRepository.save(appointment);
            AppointmentDTO updateAppointmentDTO = appointmentMapper.toDTO(updatedAppointment);

            // 감정 기록들 수정하기
            if (appointment.getMindlogs() != null) {
                for (MindlogDTO mindlogDTO : appointmentDTO1.getMindlogDTOs()) {
                    MindlogDTO saveMindlogDTO = mindlogService.updateMindlog(mindlogDTO.getId(), mindlogDTO);
                    updateAppointmentDTO.getMindlogDTOs().add(saveMindlogDTO);
                    System.out.println("감정 기록도 수정됨");
                }
            }

            return updateAppointmentDTO;
        } else {
            return null;
        }
    }

    // 진료 삭제
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

    // 진료 조회 (녹음)
    public AppointmentDTO getAppointmentByRecordId(Long recordId) {
        Optional<Appointment> appointment = appointmentRepository.findByRecordId(recordId);
        return appointment.map(appointmentMapper::toDTO).orElse(null);
    }

    // 녹음을 추가했을 때 진료 일정 수정하기
    public AppointmentDTO addRecordToAppointment(Long appointmentId, RecordDTO recordDTO) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                 .orElseThrow(() -> new ResourceNotFoundException("Appointment not found"));

        // Record를 저장하고 Appointment에 설정
        Record record = recordMapper.toEntity(recordDTO);
        Record savedRecord = recordRepository.save(record);
        appointment.setRecord(savedRecord);
        Appointment savedAppointment = appointmentRepository.save(appointment);

        // AppointmentDTO로 변환하여 반환
        AppointmentDTO appointmentDTO = new AppointmentMapper().toDTO(savedAppointment);
        return appointmentDTO;
    }
}
