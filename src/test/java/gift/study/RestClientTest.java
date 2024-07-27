package gift.study;


import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

class RestClientTest {
     private final RestClient client = RestClient.builder().build();

     @Test
     void test1() {
         var url = "https://kauth.kakao.com/oauth/token";
         var body = createBody();

         var response = client.post()
                 .uri(URI.create(url))
                 .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                 .body(body)
                 .retrieve()
                 .toEntity(String.class);

         assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
         System.out.println(response);
     }

    private static @NotNull LinkedMultiValueMap<String, String> createBody() {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code"); // authorization_code로 고정
        body.add("client_id", "10ed49de30f18de4579a06a07400e0ab"); // 앱 REST API 키 [내 애플리케이션] > [앱 키]에서 확인 가능
        body.add("redirect_uri", "http://localhost:8080"); // 인가 코드가 리다이렉트된 URI
        body.add("code", "cn0rAyUWH8P0lDGROHPewIe1SxKxNSVpaAtlErVoUQRdQTkUnuRFVAAAAAQKPXRoAAABkNgpt5ensOtctwzlGQ"); // 토큰 발급 시, 보안을 강화하기 위해 추가 확인하는 코드

        return body;
    }
}
