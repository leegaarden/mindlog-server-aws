package com.mindbridge.server.service;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.dto.RecordDTO;
import com.mindbridge.server.exception.ResourceNotFoundException;
import com.mindbridge.server.model.Appointment;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.model.Record;
import com.mindbridge.server.repository.AppointmentRepository;
import com.mindbridge.server.repository.MindlogRepository;
import com.mindbridge.server.repository.RecordRepository;
import com.mindbridge.server.util.AppointmentMapper;
import com.mindbridge.server.util.MindlogMapper;
import com.mindbridge.server.util.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalTime;
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

    @Autowired
    private MindlogMapper mindlogMapper;

    @Autowired
    private MindlogRepository mindlogRepository;

    private static final LocalTime ZERO_TIME = LocalTime.MIDNIGHT;
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
        // Appointment savedAppointment = appointmentRepository.save(appointment);

        // 추가한 진료보다 앞의 진료 중 가장 가까운 진료의 감정기록들 수정
        Pageable pageable = PageRequest.of(0, 1); // limit 1
        List<Appointment> appointments =
                appointmentRepository.findAppointmentsBeforeRecordTime(appointment.getDate(), ZERO_TIME, pageable);

        // 가장 가까운 진료
        Appointment frontAppointment = new Appointment();

        if (!appointments.isEmpty()) {
            frontAppointment = appointments.get(0);

            // 감정 기록들 수정하기
            if (frontAppointment.getMindlogs() != null) {
                for (Mindlog mindlog : frontAppointment.getMindlogs()) {
                    List<Appointment> appointment1s =
                            appointmentRepository.findAppointmentsBeforeRecordTime(mindlog.getDate(), ZERO_TIME, pageable);

                    if (!appointment1s.isEmpty()) {
                        Appointment appointment1 = appointment1s.get(0);
                        // 수정된 부분이 있을 경우에만 감정기록 수정
                        if (appointment1.getId() != frontAppointment.getId()) {
                            mindlog.setAppointment(appointment1);

                            // 감정 기록을 업데이트하기 위해 필요한 서비스 호출
                            mindlogService.updateMindlog(mindlog.getId(), mindlogMapper.toDTO(mindlog));
                        }
                    }
//
//                    MindlogDTO saveMindlogDTO = mindlogService.updateMindlog(mindlogDTO.getId(), mindlogDTO);
//                    updateAppointmentDTO.getMindlogDTOs().add(saveMindlogDTO);
//                    System.out.println("감정 기록도 수정됨")
                }
            }
        } else { // 추가한 진료 일정보다 앞에 있는 일정은 없는데, 그 전의 감정 기록들은 수정해야 함. 시발
            List<Mindlog> beforeByDate = mindlogRepository.findBeforeDate(appointment.getDate());

            for (Mindlog mindlog : beforeByDate) {
                mindlog.setAppointment(appointment);
                // 감정 기록을 업데이트하기 위해 필요한 서비스 호출
                mindlogService.updateMindlog(mindlog.getId(), mindlogMapper.toDTO(mindlog));
//                if (mindlog.getAppointment().getId() == null) {
//                    mindlog.setAppointment(savedAppointment);
//                    // 감정 기록을 업데이트하기 위해 필요한 서비스 호출
//                    mindlogService.updateMindlog(mindlog.getId(), mindlogMapper.toDTO(mindlog));
//                }
            }

        }
// 날짜 수정 기능 때문에 진료 일정이랑 비교해야 함
//
//        if (appointments.isEmpty()) {
//            // Appointment 생성을 위한 필수 데이터 설정
//            appointment = new Appointment(0l);
//            appointment.setDate(mindlog.getDate());
//            appointment = appointmentRepository.save(appointment);
//        } else {
//            appointment = appointments.get(0);
//        }
//
//        mindlog.setAppointment(appointment);
//        Mindlog updatedMindlog = mindlogRepository.save(mindlog);
//        return mindlogMapper.toDTO(updatedMindlog);

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
