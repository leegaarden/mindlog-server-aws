package com.mindbridge.server.controller;

import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.repository.MindlogRepository;
import com.mindbridge.server.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {

    /*
    * 구현 필요 기능
    * 1. 이럴 때 기분이  안 좋았어요 - mindlog의 moodColor이 1,2 인 emotionRecord, eventRecord의 요약
    * 2. 이럴 때 기분이 좋았어요 - mindlog의 moodColor이 4,5 인 emotionRecord, eventRecord의 요약
    * 3. 키워드 추출 -> 세 가지 기록 전체 갖고오기
    * */

    @Autowired
    private StatsService statsService;

    @Autowired
    private MindlogRepository mindlogRepository;


    // 키워드 추출
    @GetMapping("/keyword")
    public List<String> getExtractedKeywords() {
        // FastAPI로 데이터 전송
        statsService.sendDataToFastAPI();
        System.out.println("Data sent to FastAPI successfully!");
        // 추출된 키워드 반환
        return statsService.getExtractedKeywords();
    }


    // 일단 부정, 긍정 내용 조회하는 기능
    @GetMapping("/negative")
    public List<String> getMindlogByNegativeMood () {
        return statsService.getMindlogByNegativeMood();
    }

    @GetMapping("/positive")
    public List<String> getMindlogByPositiveMood () {
        return statsService.getMindlogByPositiveMood();
    }
}
