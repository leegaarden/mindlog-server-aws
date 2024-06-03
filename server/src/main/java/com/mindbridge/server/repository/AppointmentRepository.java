package com.mindbridge.server.repository;

import com.mindbridge.server.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    // 날짜로 조회
    @Query("SELECT a FROM Appointment a WHERE a.date = :date")
    List<Appointment> findByDate(@Param("date") Date date);

    // 녹음 아이디로 조회
    @Query("SELECT a FROM Appointment a WHERE a.record.id = :recordId")
    Optional<Appointment> findByRecordId(@Param("recordId") Long recordId);

    // 가장 최근의 appointment조회
    @Query("select a from Appointment a order by a.date DESC, a.startTime DESC limit 1")
    Appointment findTopByOrderByDateDescStartTimeDesc();



}
