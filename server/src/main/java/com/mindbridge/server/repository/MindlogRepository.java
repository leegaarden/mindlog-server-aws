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
}
