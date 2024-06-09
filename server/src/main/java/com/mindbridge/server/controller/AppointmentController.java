package com.mindbridge.server.controller;

import com.mindbridge.server.dto.AppointmentDTO;
import com.mindbridge.server.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointment")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    // 진료 목록 전체 조회
    @GetMapping
    public List<AppointmentDTO> getAllAppointments() {
        return appointmentService.getAllAppointments();
    }

    // 진료 추가
    @PostMapping
    public ResponseEntity<AppointmentDTO> addAppointment(@RequestBody AppointmentDTO appointmentDTO) {

        AppointmentDTO addedAppointment = appointmentService.addAppointment(appointmentDTO);

        if (addedAppointment != null) {
            System.out.println("진료 추가 성공");
            System.out.println("병원: " + addedAppointment.getHospital());
            return ResponseEntity.ok(addedAppointment);
        } else {
            System.out.println("진료 추가 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 진료 조회 (개별)
    @GetMapping("/{id}")
    public AppointmentDTO getAppointmentById(@PathVariable Long id) {
        AppointmentDTO appointmentDTO = appointmentService.getAppointmentById(id);
        if (appointmentDTO != null) {
            System.out.println("진료 조회(개별) 성공");
            System.out.println("진료 조회(개별) 병원: " + appointmentDTO.getHospital());
            return appointmentDTO;
        } else {
            System.out.println("진료 조회(개별) 실패");
            return null;
        }
    }

    // 진료 조회 (날짜별)
    @GetMapping("/by-date/{date}")
    public List<AppointmentDTO> getAppointmentsByDate(@PathVariable Date date) {

        List<AppointmentDTO> appointmentDTOS = appointmentService.
                getAppointmentsByDate(date);

        System.out.println("진료 조회(날짜별): " + date);
        for(AppointmentDTO appointmentDTO : appointmentDTOS) {
            System.out.println("병원: " + appointmentDTO.getHospital());
        }

        return appointmentDTOS;
    }

    // 녹음 조회용 컨트롤러 필요
    @GetMapping("/by-record/{recordId}")
    public AppointmentDTO getAppointmentByRecordId(@PathVariable Long recordId) {

        AppointmentDTO appointmentDTO = appointmentService.
                getAppointmentByRecordId(recordId);

        System.out.println("진료 조회(녹음): " + recordId);
        return appointmentDTO;
    }


    // 진료 수정
    @PutMapping("/{id}")
    public AppointmentDTO updateAppointment(@PathVariable Long id, @RequestBody AppointmentDTO appointmentDTO) {

        AppointmentDTO updatedAppointment = appointmentService.updateAppointment(id, appointmentDTO);
        if (updatedAppointment != null) {
            System.out.println("진료 수정 성공");
            System.out.println("수정 ID: " + id);
            return updatedAppointment;
        } else {
            System.out.println("진료 수정 실패");
            return null;
        }
    }

    // 진료 삭제
    @DeleteMapping("/{id}")
    public void deleteAppointment(@PathVariable Long id) {
        System.out.println("감정 기록 삭제 ID: " + id);
        appointmentService.deleteAppointment(id);
    }

}
