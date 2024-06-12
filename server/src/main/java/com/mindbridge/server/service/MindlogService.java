package com.mindbridge.server.service;


import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Appointment;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.repository.AppointmentRepository;
import com.mindbridge.server.repository.MindlogRepository;
import com.mindbridge.server.util.MindlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MindlogService {

    @Autowired
    private MindlogRepository mindlogRepository;

    @Autowired
    private MindlogMapper mindlogMapper;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private StatsService statsService;

    // 전부 조회
    public List<MindlogDTO> getAllMindlogs() {
        List<Mindlog> mindlogs = mindlogRepository.findAll();
        return mindlogs.stream()
                .map(mindlogMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 감정 기록 추가
    public MindlogDTO addMindlog(MindlogDTO mindlogDTO) {
        Mindlog mindlog = mindlogMapper.toEntity(mindlogDTO);

        try {
            // 요약 먼저 하기
            List<String> summaryData = new ArrayList<>();

            // json으로 바꿔보기
            //String data = " \" " + mindlog.getEmotionRecord() + " \" ";

            summaryData.add(mindlog.getEmotionRecord());
            String emotionSummary = statsService.callExternalApi(summaryData);
            mindlog.setEmotionSummary(emotionSummary != null ? emotionSummary : "요약 실패");

            summaryData.set(0, mindlog.getEventRecord());
            String eventSummary = statsService.callExternalApi(summaryData);
            mindlog.setEventSummary(eventSummary != null ? eventSummary : "요약 실패");

            summaryData.set(0, mindlog.getQuestionRecord());
            String questionSummary = statsService.callExternalApi(summaryData);
            mindlog.setQuestionSummary(questionSummary != null ? questionSummary : "요약 실패");

            // 통계용..
            summaryData.set(0, mindlog.getEmotionEvent());
            String emotionEventSummary = statsService.callExternalApi(summaryData);
            mindlog.setEmotionEventSummary(emotionEventSummary != null ? emotionEventSummary : "요약 실패");

        } catch (Exception e) {
            // 예외 발생 시 기본값 설정
            mindlog.setEmotionSummary("감정 요약 실패");
            mindlog.setEventSummary("이벤트 요약 실패");
            mindlog.setQuestionSummary("질문 요약 실패");
            mindlog.setEmotionEventSummary("통계 요약 실패");
            e.printStackTrace();
        }

        Pageable pageable = PageRequest.of(0, 1); // limit 1
        List<Appointment> appointments =
                appointmentRepository.findAppointmentsBeforeRecordTime(mindlog.getDate(),pageable);

        Appointment appointment;
        if (appointments.isEmpty()) {
            // Appointment 생성을 위한 필수 데이터 설정
            appointment = new Appointment(0l);
            appointment.setDate(mindlog.getDate());
            appointment = appointmentRepository.save(appointment);
        } else {
            appointment = appointments.get(0);
        }

        //appointment.setId(0l);
        mindlog.setAppointment(appointment);
        Mindlog savedMindlog = mindlogRepository.save(mindlog);
        savedMindlog.getAppointment().setId(0l);
        return mindlogMapper.toDTO(savedMindlog);
    }


    // 감정 기록 조회 (개별)
    public MindlogDTO getMindlogById(Long id) {
        Mindlog mindlog = mindlogRepository.findById(id).orElse(null);
        return mindlog != null ? mindlogMapper.toDTO(mindlog) : null;
    }

    // 감정 기록 조회 (날짜)
    public List<MindlogDTO> getMindlogsByDate(Date date) {
        List<Mindlog> mindlogs = mindlogRepository.findByDate(date);
        return mindlogs.stream()
                .map(mindlogMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 진료 일정별 시간 순서대로 mindlog 조회 -> 모아보기
    public List<MindlogDTO> getMindlogsByAppointmentId(Long appointmentId) {
        List<Mindlog> mindlogs = mindlogRepository.findByAppointmentIdOrderByCreatedAtAsc(appointmentId);
        return mindlogs.stream()
                .map(mindlogMapper::toDTO)
                .collect(Collectors.toList());
    }

    // 모아보기 전체 조회
//    public List<List<MindlogDTO>> getMindlogsByAppointmentIdByDate() {
//        List<Appointment> appointments = appointmentRepository.findAll();
//        List<List<MindlogDTO>> returnMindlogDTOs = new ArrayList<>();
//
//        for (Appointment appointment : appointments) {
//            Long appointmentId = appointment.getId();
//            List<MindlogDTO> mindlogDTOs = getMindlogsByAppointmentId(appointmentId);
//            returnMindlogDTOs.add(mindlogDTOs);
//        }
//
//        return returnMindlogDTOs;
//    }
    // 감정 기록 수정
    public MindlogDTO updateMindlog(Long id, MindlogDTO mindlogDTO) {

        if (mindlogRepository.existsById(id)) {
            Mindlog mindlog = mindlogMapper.toEntity(mindlogDTO);
            mindlog.setId(id);

            // 감정 기록을 수정하면 요약도 변경되기 때문에 수정해야 함.
            try {

                List<String> summaryData = new ArrayList<>();

                summaryData.add(mindlog.getEmotionRecord());
                String emotionSummary = statsService.callExternalApi(summaryData);
                mindlog.setEmotionSummary(emotionSummary != null ? emotionSummary : "요약 실패");

                summaryData.set(0, mindlog.getEventRecord());
                String eventSummary = statsService.callExternalApi(summaryData);
                mindlog.setEventSummary(eventSummary != null ? eventSummary : "요약 실패");

                summaryData.set(0, mindlog.getQuestionRecord());
                String questionSummary = statsService.callExternalApi(summaryData);
                mindlog.setQuestionSummary(questionSummary != null ? questionSummary : "요약 실패");

                // 통계용..
                summaryData.set(0, mindlog.getEmotionEvent());
                String emotionEventSummary = statsService.callExternalApi(summaryData);
                mindlog.setEmotionEventSummary(emotionEventSummary != null ? emotionEventSummary : "요약 실패");

            } catch (Exception e) {
                // 예외 발생 시 기본값 설정
                mindlog.setEmotionSummary("감정 요약 실패");
                mindlog.setEventSummary("이벤트 요약 실패");
                mindlog.setQuestionSummary("질문 요약 실패");
                mindlog.setEmotionEventSummary("통계 요약 실패");
                e.printStackTrace();
            }

            // 날짜 수정 기능 때문에 진료 일정이랑 비교해야 함
            Pageable pageable = PageRequest.of(0, 1); // limit 1
            List<Appointment> appointments =
                    appointmentRepository.findAppointmentsBeforeRecordTime(mindlog.getDate(),pageable);

            Appointment appointment;
            if (appointments.isEmpty()) {
                // Appointment 생성을 위한 필수 데이터 설정
                appointment = new Appointment(0l);
                appointment.setDate(mindlog.getDate());
                appointment = appointmentRepository.save(appointment);
            } else {
                appointment = appointments.get(0);
            }

            mindlog.setAppointment(appointment);
            Mindlog updatedMindlog = mindlogRepository.save(mindlog);
            return mindlogMapper.toDTO(updatedMindlog);
        } else {
            return null;
        }
    }

    // 감정 기록 삭제
    public void deleteMindlog(Long id) {
        mindlogRepository.deleteById(id);
    }
}
