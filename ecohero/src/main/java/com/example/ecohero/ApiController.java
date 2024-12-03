package com.example.ecohero;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

    private final ApiService apiService;

    // 생성자 주입을 통해 ApiService 의존성 주입
    public ApiController(ApiService apiService) {
        this.apiService = apiService;
    }

    // 사용자가 /fetch-data 엔드포인트로 GET 요청을 보낼 때 API 호출
    @GetMapping("/fetch-data")
    public String fetchData(@RequestParam String url) {
        return apiService.getDataFromApi(url);
    }
}
