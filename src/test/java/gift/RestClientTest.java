package gift;

import gift.config.KakaoProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;

import java.net.URI;

@ActiveProfiles("test")
@SpringBootTest
public class RestClientTest {

    @Autowired
    private KakaoProperties kakaoProperties;
    private RestClient client = RestClient.builder()
            .defaultStatusHandler(HttpStatusCode::is4xxClientError,((request, response) -> {
                throw new RestClientException("에러");
            }))
            .build();

    @Test
    void test2() {
        System.out.println(kakaoProperties);
        Assertions.assertThat(kakaoProperties.redirectURL()).isEqualTo("redirect");
    }

    @Test
    void test1() {
        var url = "https://kauth.kakao.com/oauth/token";
        //원래는 spring이 주입할 수 있도록 해야됨!
//        var kakaoProperties = new KakaoProperties("1fdc4eeb45ccb5f6a2f6e005c7810546", "http://localhost:8080/redirect/kakao");
        var body = new LinkedMultiValueMap<String, String>();
        //form 타입이라 body 생성해야됨
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectURL());
        body.add("code", "d8vNGhAbEewKZL5791tJwpJxaPxFY6BGHyxqr0mYQi7KDZGhLG_44QAAAAQKKiWOAAABkO2wLlqoblpFv_zasg");
        //1. body : request, 2 body: response
        var response = client.post()
                .uri(URI.create(url))
                .header("Authorization", "BEARER <Token>")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(String.class);
                //.body(String.class); 응답을 string으로 받음
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println(response);
    }
}
