package com.mindbridge.server.service;

import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.repository.MindlogRepository;
import com.mindbridge.server.util.MindlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MindlogService {

    @Autowired
    private MindlogRepository mindlogRepository;

    @Autowired
    private MindlogMapper mindlogMapper;

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
        Mindlog savedMindlog = mindlogRepository.save(mindlog);
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


    // 감정 기록 수정
    public MindlogDTO updateMindlog(Long id, MindlogDTO mindlogDTO) {
        if (mindlogRepository.existsById(id)) {
            Mindlog mindlog = mindlogMapper.toEntity(mindlogDTO);
            mindlog.setId(id);
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
