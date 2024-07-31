//package gift.study;
//
//import jakarta.validation.constraints.NotNull;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.web.client.RestTemplateBuilder;
//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URI;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//
//public class RestTemplateTest {
//    private final RestTemplate client = new RestTemplateBuilder().build();
//
//    @Test
//    void test1() {
//        var url = "https://kauth.kakao.com/oauth/token";
//
//
//        /// 1. 요청
//        // 헤더
//        var headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
//        // 본문
//        final var body = createBody();
//        var request = new RequestEntity<>(body, headers, HttpMethod.POST, URI.create(url));
//
//        /// 2. 응답
//        var response = client.exchange(request, String.class);
//        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
//        System.out.println(response);
//    }
//
//    private static @NotNull LinkedMultiValueMap<String, String> createBody() {
//        var code = "cn0rAyUWH8P0lDGROHPewIe1SxKxNSVpaAtlErVoUQRdQTkUnuRFVAAAAAQKPXRoAAABkNgpt5ensOtctwzlGQ";
//        var body = new LinkedMultiValueMap<String, String>();
//        body.add("grant_type", "authorization_code"); // authorization_code로 고정
//        body.add("client_id", "10ed49de30f18de4579a06a07400e0ab"); // 앱 REST API 키 [내 애플리케이션] > [앱 키]에서 확인 가능
//        body.add("redirect_uri", "http://localhost:8080"); // 인가 코드가 리다이렉트된 URI
//        body.add("code", code); // 토큰 발급 시, 보안을 강화하기 위해 추가 확인하는 코드
//
//        return body;
//    }
//
//}
