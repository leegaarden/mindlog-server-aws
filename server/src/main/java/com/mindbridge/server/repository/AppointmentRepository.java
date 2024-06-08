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

//    @Query("SELECT NULLIF(Appointment, 0) FROM Appointment a WHERE a.date > :mindlogDate ORDER BY a.startTime DESC limit 1")
//    Appointment findAppointmentsBeforeRecordTime(@Param("mindlogDate") Date mindlogDate);

    // mindlogDate 이전의 appointment 조회
    @Query("SELECT a FROM Appointment a WHERE a.date > :mindlogDate ORDER BY a.startTime DESC")
    List<Appointment> findAppointmentsBeforeRecordTime(@Param("mindlogDate") Date mindlogDate, Pageable pageable);

}
