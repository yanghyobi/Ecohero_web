recycle-chatbot/
 ├── src/
 │    ├── main/
 │    │    ├── java/
 │    │    │    └── com/
 │    │    │         └── example/
 │    │    │               └── chatbot/
 │    │    │                     ├── ChatbotApplication.java  # 메인 애플리케이션 파일
 │    │    │                     ├── config/
 │    │    │                     │     └── AppConfig.java       # RestTemplate 설정 파일
 │    │    │                     ├── controller/
 │    │    │                     │     └── ChatbotController.java   # 챗봇 API 컨트롤러
 │    │    │                     ├── entity/
 │    │    │                     │     └── RecyclingRule.java   # 엔티티 클래스
 │    │    │                     ├── repository/
 │    │    │                     │     └── RecyclingRuleRepository.java  # JPA 리포지토리
 │    │    │                     └── service/
 │    │    │                           └── ChatbotService.java      # 챗봇 서비스 로직
 │    └── resources/
 │          ├── static/
 │          │     └── chatbot.js  # 프론트엔드 JS 코드
 │          ├── templates/
 │          │     └── chatbot.html  # 프론트엔드 HTML 코드
 │          ├── application.properties   # 설정 파일
 └── pom.xml  # Maven 의존성
