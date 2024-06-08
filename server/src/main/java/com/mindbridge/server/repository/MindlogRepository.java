package com.mindbridge.server.repository;

import com.mindbridge.server.model.Mindlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;

public interface MindlogRepository extends JpaRepository<Mindlog, Long> {
    @Query("SELECT m FROM Mindlog m WHERE m.date = :date")
    List<Mindlog> findByDate(@Param("date") Date date);

    // 진료 아이디별 mindlog 조회
    @Query("SELECT m FROM Mindlog m WHERE m.appointment.id = :appointmentId ORDER BY m.createdAt ASC")
    List<Mindlog> findByAppointmentIdOrderByCreatedAtAsc(@Param("appointmentId") Long appointmentId);

    // allRecord 조회
    @Query("SELECT m.allRecord FROM Mindlog m")
    List<String> findAllRecords();

    // 부정 감정 기록 조회 -> moodColor가 1, 2
    @Query("SELECT m.emotionEventSummary FROM Mindlog m WHERE m.moodColor = 1 or m.moodColor = 2")
    List<String> findByNegativeMindlogs();

    // 긍정 감정 기록 조회 -> moodColor가 4, 5
    @Query("SELECT m.emotionEventSummary FROM Mindlog m WHERE m.moodColor = 4 or m.moodColor = 5")
    List<String> findByPositiveMindlogs();

    // 진료 일정 아이디로 감정 기록 조회
    List<Mindlog> findByAppointmentId(Long appointmentId);

    // 요약 api 데이터 용
    @Query("SELECT m.emotionRecord, m.eventRecord, m.questionRecord FROM Mindlog m")
    List<String> fingSummaryData();
}
