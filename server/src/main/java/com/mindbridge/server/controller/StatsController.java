package com.mindbridge.server.controller;

import com.mindbridge.server.dto.MindlogDTO;
import com.mindbridge.server.model.Mindlog;
import com.mindbridge.server.repository.MindlogRepository;
import com.mindbridge.server.service.StatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {

    /*
    * 구현 필요 기능
    * 1. 이럴 때 기분이  안 좋았어요 - mindlog의 moodColor이 1,2 인 것의 요약
    * 2. 이럴 때 기분이 좋았어요 - mindlog의 moodColor이 4,5 인 것의 요약
    * => 요약문의 기준이 뭔가요? 감정, 사건, 질문인가요 아니면 전체인가요?
    * 3. 키워드 추출 -> 세 가지 기록 전체 갖고오기
    * */

    @Autowired
    private StatsService statsService;

    @Autowired
    private MindlogRepository mindlogRepository;


    @PostMapping("/keyword")
    public String sendDataToFastAPI() {
        // FastAPI로 데이터 전송
        statsService.sendDataToFastAPI();
        return "Data sent to FastAPI successfully!";
    }

    // 일단 부정, 긍정 내용 조회하는 기능 만들기 -> 요약 api 되면 요약 데이터로 바꾸기
    @GetMapping("/negative")
    public List<String> getMindlogByNegativeMood () {
        return statsService.getMindlogByNegativeMood();
    }

    @GetMapping("/positive")
    public List<String> getMindlogByPositiveMood () {
        return statsService.getMindlogByPositiveMood();
    }
}
