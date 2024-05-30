package com.mindbridge.server.controller;

import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.service.MindlogService;
import com.mindbridge.server.util.MindlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mindlog")
public class MindlogController {

    @Autowired
    private MindlogService mindlogService;

    // 감정 기록 전체 조회
    @GetMapping
    public List<MindlogDTO> getAllMindlogs() {
        return mindlogService.getAllMindlogs();
    }

    // 감정기록 추가
    @PostMapping()
    public ResponseEntity<MindlogDTO> addMindlog(@RequestBody MindlogDTO mindlogDTO) {

        MindlogDTO addedMindlog = mindlogService.addMindlog(mindlogDTO);
        if (addedMindlog != null) {
            // 새로운 감정 기록이 성공적으로 추가되었을 때
            System.out.println("감정 기록 추가 성공 ");
            System.out.println("제목: " + addedMindlog.getTitle());
            return ResponseEntity.ok(addedMindlog);
        } else {
            // 진료 감정 기록에 실패했을 때
            System.out.println("감정 기록 추가 실패");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 감정 기록 조회 (개별)
    @GetMapping("/{id}")
    public MindlogDTO getMindlogById(@PathVariable Long id) {

        MindlogDTO mindlogDTO = mindlogService.getMindlogById(id);

        if (mindlogDTO != null) {
            System.out.println("감정 기록 조회(개별) 성공");
            System.out.println("감정 기록 조회(개별) 제목: " + mindlogDTO.getTitle());
        } else {
            System.out.println("감정 기록 조회(개별) 실패");
            return null;
        }

        return mindlogDTO != null ? mindlogDTO : null;
    }

    // 감정 기록 조회 (날짜별)
    @GetMapping("/by-date/{date}")
    public List<MindlogDTO> getMindlogsByDate(@PathVariable Date date) {

        List<MindlogDTO> mindlogDTOSs= mindlogService.getMindlogsByDate(date);

        System.out.println("감정 기록 조회(날짜별): " + date);
        for (MindlogDTO mindlogDTO : mindlogDTOSs) {
            System.out.println("제목: " + mindlogDTO.getTitle());
        }

        return mindlogDTOSs;
    }

    // 감정 기록 조회(진료 일정별)
    @GetMapping("/appointment/{appointmentId}")
    public List<MindlogDTO> getMindlogsByAppointmentId(@PathVariable Long appointmentId) {
        return mindlogService.getMindlogsByAppointmentId(appointmentId);
    }

    // 감정 기록 수정
    @PutMapping("/{id}")
    public MindlogDTO updateMindlog(@PathVariable Long id, @RequestBody MindlogDTO mindlogDTO) {

        MindlogDTO updateMindlog = mindlogService.updateMindlog(id, mindlogDTO);
        if (updateMindlog != null) {
            System.out.println("감정 기록 수정 성공");
            System.out.println("수정 ID: " + id);
            return updateMindlog;
        } else {
            System.out.println("감정 기록 수정 실패");
            return null;
        }
    }

    // 감정 기록 삭제
    @DeleteMapping("/{id}")
    public void deleteMindlog(@PathVariable Long id) {
        System.out.println("감정 기록 삭제 ID: " + id);
        mindlogService.deleteMindlog(id);

    }
}
