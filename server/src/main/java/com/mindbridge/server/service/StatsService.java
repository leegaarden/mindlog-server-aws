package com.mindbridge.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mindbridge.server.repository.MindlogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.OutputStream;
import java.net.CacheRequest;
import java.util.*;
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

    // 추출된 키워드를 저장할 필드
    private List<String> extractedKeywords;


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

        // FastAPI에 POST 요청 보내기 및 응답 받기
        List<String> response = restTemplate.postForObject(fastApiUrl, requestEntity, List.class);

        // 응답 데이터 저장 및 로그 출력
        if (response != null) {
            extractedKeywords = response;
            response.forEach(keyword -> System.out.println("Extracted keyword: " + keyword));
        } else {
            System.out.println("No keywords extracted");
        }
    }

    // 추출한 키워드 클라이언트로
    public List<String> getExtractedKeywords() {
        return extractedKeywords;
    }

    public List<String> getrecordsSummarys(List<String> records) {

        List<String> recordsSummarys = new ArrayList<>();

        for (String record : records) {
            String[] data = record.split("\n");

            int size = data.length;
            for (int i = 0; i < size; i++) {
                int j = data[i].indexOf("-");
                data[i] = data[i].substring(j + 2);
                recordsSummarys.add(data[i]);
            }

        }


        return recordsSummarys;
    }

    // 부정 감정 기록 조회
    public List<String> getMindlogByNegativeMood () {

        List<String> records = mindlogRepository.findByNegativeMindlogs(); // \n 포함된 거
        List<String> recordsSummarys = getrecordsSummarys(records); // \n 없앤 거
        List<String> negativeRecords = new ArrayList<>(); // return

        int recordsSummarysSize = recordsSummarys.size();

        Random random = new Random();
        List<Integer> idx = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int g = random.nextInt(recordsSummarysSize);
            idx.add(g);

            for (int j = 0; j < i; j++) { // 중복 제거
                if (idx.get(i) == idx.get(j)) {
                    i--;
                    break;
                }
            }
        }

        negativeRecords.add(recordsSummarys.get(idx.get(0)));
        negativeRecords.add(recordsSummarys.get(idx.get(1)));
        negativeRecords.add(recordsSummarys.get(idx.get(2)));

        return negativeRecords;

    }

    // 긍정 감정 기록 조회
    public List<String> getMindlogByPositiveMood () {
        List<String> records = mindlogRepository.findByPositiveMindlogs();
        List<String> recordsSummarys = getrecordsSummarys(records); // \n 없앤 거
        List<String> positiveRecords = new ArrayList<>(); // return

        int recordsSummarysSize = recordsSummarys.size();

        Random random = new Random();
        List<Integer> idx = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int g = random.nextInt(recordsSummarysSize);
            idx.add(g);

            for (int j = 0; j < i; j++) { // 중복 제거
                if (idx.get(i) == idx.get(j)) {
                    i--;
                    break;
                }
            }
        }

        positiveRecords.add(recordsSummarys.get(idx.get(0)));
        positiveRecords.add(recordsSummarys.get(idx.get(1)));
        positiveRecords.add(recordsSummarys.get(idx.get(2)));

        return positiveRecords;
    }


    public String callExternalApi(List<String> texts) throws JsonProcessingException { // 리스트로 받음

        // 요약api url
        String url = "https://clovastudio.apigw.ntruss.com/testapp/v1/api-tools/summarization/v2/784a3775e1d14cadbb0ab588efa2956f";

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

        try {
            ObjectMapper mapper = new ObjectMapper();
            //String jsonRequestBody = mapper.writeValueAsString(requestBody);
            //String jsonRequestHeader = mapper.writeValueAsString(headers);

            // HttpEntity<String> entity = new HttpEntity<>(jsonRequestBody, headers);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

            int statusCode = response.getStatusCodeValue();  // 응답 코드 가져오기
            HttpStatus statusText = (HttpStatus) response.getStatusCode();  // 응답 코드 설명 가져오기

            if (response.getStatusCode().is2xxSuccessful()) {
                JsonNode root = mapper.readTree(response.getBody());
                String text = root.path("result").path("text").asText();
                System.out.println("Response Code: " + statusCode + " (" + statusText + ")");
                return text;
            } else {
                // 오류 응답 처리
                System.out.println("Error: " + statusCode + " (" + statusText + ")");
                System.out.println("Response Body: " + response.getBody());
                return null;
            }
        } catch (HttpClientErrorException e) {
            // 클라이언트 오류 (4xx) 처리
            System.out.println("HTTP Status Code: " + e.getStatusCode());
            System.out.println("HTTP Response Body: " + e.getResponseBodyAsString());
            System.out.println("Request URL: " + url);
            System.out.println("Request Headers: " + headers);
            try {
                ObjectMapper mapper = new ObjectMapper();
                String jsonRequestBody = mapper.writeValueAsString(requestBody);
                System.out.println("Request Body: " + jsonRequestBody);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return null;
        } catch (HttpServerErrorException e) {
            // 서버 오류 (5xx) 처리
            System.out.println("HTTP Status Code: " + e.getStatusCode());
            System.out.println("HTTP Response Body: " + e.getResponseBodyAsString());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            // 기타 예외 처리
            e.printStackTrace();
            return null;
        }

    }
}
