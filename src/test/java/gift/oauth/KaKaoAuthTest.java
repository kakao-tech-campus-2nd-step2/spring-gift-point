package gift.oauth;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.oauth.business.dto.KakaoParam;
import gift.oauth.business.dto.OAuthParam;
import gift.oauth.business.dto.OAuthToken;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
public class KaKaoAuthTest {

    @Autowired
    private Environment env;

    private final RestClient restClient;

    public KaKaoAuthTest() {
        this.restClient = RestClient.create();
    }

    @Test
    void test() {
        // given
        String code = "0VD_ojXscl1UzQhz81SW3_fiBqxe8PFHDEw24SGO803vAJot331TwAAAAAQKKiUPAAABkOAaPx7E017PSiBv1Q";
        OAuthParam param = new KakaoParam(env, code);

        // when
        var url = "https://kauth.kakao.com/oauth/token";

        var result =  restClient.post()
            .uri(url)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(param.getTokenRequestBody())
            .retrieve()
            .onStatus(
                HttpStatusCode::isError,
                (request, response) -> {
                    throw new RuntimeException("Failed to get access token from Kakao API.");
                }
            )
            .body(OAuthToken.Kakao.class);

        // then
        assertAll (
            () -> assertNotNull(result),
            () -> assertNotNull(result.access_token())
        );
    }

}
