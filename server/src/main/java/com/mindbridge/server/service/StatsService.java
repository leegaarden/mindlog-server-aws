package com.mindbridge.server.service;

import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.repository.MindlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService {

    @Autowired
    private MindlogRepository mindlogRepository;

    private final RestTemplate restTemplate;

    @Autowired
    public StatsService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 키워드 추출 FastAPI
    public void sendDataToFastAPI() {
        // FastAPI 엔드포인트 URL
        String fastApiUrl = "http://43.200.38.68:8000/process/";

        // data
        List<String> data = mindlogRepository.findAllRecords();

        // 요청 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // 요청 바디 설정
        HttpEntity<List<String>> requestEntity = new HttpEntity<>(data, headers);

        // FastAPI에 POST 요청 보내기
        restTemplate.postForObject(fastApiUrl, requestEntity, String.class);
    }

    // 부정 감정 기록 조회
    public List<String> getMindlogByNegativeMood () {
        List<String> negativeRecords = mindlogRepository.findByNegativeMindlogs();
        return negativeRecords;
    }

    // 긍정 감정 기록 조회
    public List<String> getMindlogByPositiveMood () {
        List<String> positiveRecords = mindlogRepository.findByPositiveMindlogs();
        return positiveRecords;
    }
}
