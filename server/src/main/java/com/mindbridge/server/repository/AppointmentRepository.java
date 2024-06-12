package com.mindbridge.server.repository;

import com.mindbridge.server.model.Appointment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 날짜로 조회
    @Query("SELECT a FROM Appointment a WHERE a.date = :date")
    List<Appointment> findByDate(@Param("date") Date date);

    // 녹음 아이디로 조회
    @Query("SELECT a FROM Appointment a WHERE a.record.id = :recordId")
    Optional<Appointment> findByRecordId(@Param("recordId") Long recordId);

    // 특정 감정기록 작성 날짜 이전의 진료 값 중 가장 최신의 진료 일정 조회
    @Query("SELECT a FROM Appointment a WHERE a.date > :mindlogDate ORDER BY a.date, a.startTime")
    List<Appointment> findAppointmentsBeforeRecordTime(@Param("mindlogDate") Date mindlogDate, Pageable pageable);

    // 전체 조회문 수정(생성 일시별로에서 진료 일정 별로)
    @Override
    @Query("SELECT a FROM Appointment a ORDER BY a.date, a.startTime")
    List<Appointment> findAll();
}
