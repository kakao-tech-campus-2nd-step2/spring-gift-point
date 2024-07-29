package gift.kakao;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;

@SpringBootTest
public class LoginTest {
    @Autowired
    RestClient client;

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;
    @Test
    void test1(){
        var url = "https://kauth.kakao.com/oauth/authorize?scope=talk_message&response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        var response = client.post()
            .uri(URI.create(url))
            .retrieve()
            .toEntity(String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(302));
        System.out.println(response);
    }

//    private static @NotNull LinkedMultiValueMap<String, String> createBody(){
//        var body = new LinkedMultiValueMap<String, String>();
//        body.add("client_id", clientId);
//        body.add("redirect_url", redirectUrl);
//        body.add("response_type", "code");
//        return body;
//    }
}
