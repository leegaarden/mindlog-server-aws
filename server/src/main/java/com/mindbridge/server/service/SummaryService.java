package com.mindbridge.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SummaryService {

    private final RestTemplate restTemplate;

    @Autowired
    public SummaryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public String callExternalApi(List<String> texts) { // 리스트로 받음

        // 요약api url
        String url = "https://clovastudio.apigw.ntruss.com/testapp/v1/apitools/summarization/v2/90061e1d2ef741319a6fa3ee42d504ad";

        // 헤더 선언(인증키)
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-CLOVASTUDIO-API-KEY","NTA0MjU2MWZlZTcxNDJiYxsD7b4uMxHvvPOsvNRFZEAid8s8O/oQCkTgDOsEZ0h5");
        headers.set("X-NCP-APIGW-API-KEY", "cjUDnGePNBMIpwZAxoMRdJarMgIeuuyGMib2RsK4");
        // headers.set("X-NCP-CLOVASTUDIO-REQUEST-ID", "e3aa2c2e-6caa-4a47-8247-9cb9d72e3d28");
        headers.set("Content-Type", "application/json");

        // 요청 바디(texts가 리스트타입이고 나머지는 하이퍼파라미터입니다)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("texts", texts);
        requestBody.put("segMinSize", 300);
        requestBody.put("includeAiFilters", true);
        requestBody.put("autoSentenceSplitter", true);
        requestBody.put("segCount", -1);
        requestBody.put("segMaxSize", 1000);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // POST 요청, 스트링으로 응답 받기
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        int statusCode = response.getStatusCodeValue();  // 응답 코드
        HttpStatus status = (HttpStatus) response.getStatusCode();  // 응답 코드 객체  // 응답 코드 설명

        // 응답 처리
        if (response.getStatusCode().is2xxSuccessful()) {   // 응답코드가 200대(성공)
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode root = mapper.readTree(response.getBody());
                String text = root.path("result").path("text").asText();
                System.out.println("Response Code: " + statusCode + " (" + status + ")");
                return text;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            // 오류 응답 처리(실패)
            System.out.println("Error: " + statusCode + " (" + status + ")");
            System.out.println("Response Body: " + response.getBody());
            return null;
        }
    }
}

