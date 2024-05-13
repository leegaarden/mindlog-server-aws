package com.mindbridge.server.controller;

import com.mindbridge.server.dto.RecordDTO;
import com.mindbridge.server.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/v1/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    // 녹음 생성
    @PostMapping
    public ResponseEntity<RecordDTO> createRecord(@RequestBody RecordDTO recordDTO) {

        RecordDTO addedRecord = recordService.addRecord(recordDTO);
        if (addedRecord != null) {
            return ResponseEntity.ok(addedRecord);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // 녹음 재생(조회)
    @GetMapping("/{id}")
    public RecordDTO getRecordById(@PathVariable Long id) {
        return recordService.getRecordById(id);
    }

    // 녹음 재생 ( appoinment 개별 조회했을 때 path 드림 -> 걍 path만 드리면
//    @GetMapping(value = "/{id}/play", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
//    public ResponseEntity<byte[]> playRecord(@PathVariable Long id) {
//        byte[] fileContent = recordService.getRecordFileContent(id);
//        if (fileContent != null) {
//            return ResponseEntity.ok().body(fileContent);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//        }
//    }

    // 녹음 삭제
    @DeleteMapping("/{id}")
    public void deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
    }
}
