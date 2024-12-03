package com.example.ecohero;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {

    private final RestTemplate restTemplate;

    // 생성자를 통한 RestTemplate 주입
    public ApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    // 외부 API 호출 (GET 요청)
    // 외부 API로 GET 요청을 보내는 메서드
    public String getDataFromApi(String url) {
        return restTemplate.getForObject(url, String.class);
    }
}
