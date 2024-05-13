package com.mindbridge.server.service;

import com.mindbridge.server.dto.RecordDTO;
import com.mindbridge.server.model.Record;
import com.mindbridge.server.repository.RecordRepository;
import com.mindbridge.server.util.RecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordService {
    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private RecordMapper recordMapper;

    // default 생성자
    public RecordService() {
    }

    // 녹음 추가
    public RecordDTO addRecord(RecordDTO recordDTO) {
        Record record = recordMapper.toEntity(recordDTO);
        Record savedRecord = recordRepository.save(record);
        return recordMapper.toDTO(savedRecord);
    }

    // 녹음 조회 (개별)
    public RecordDTO getRecordById(Long id) {
        Record record = recordRepository.findById(id).orElse(null);
        return record != null ? recordMapper.toDTO(record) : null;
    }

    // 녹음 삭제
    public void deleteRecord(Long id) {
        recordRepository.deleteById(id);
    }

    // 녹음 파일 경로 조회
    public String getRecordFilePath(Long id) {
        Record record = recordRepository.findById(id).orElse(null);
        return record != null ? record.getFilePath() : null;
    }
}
