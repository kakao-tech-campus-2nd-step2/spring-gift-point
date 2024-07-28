package gift.auth;

import gift.auth.dto.KakaoAccessToken;
import gift.auth.dto.KakaoProperties;
import gift.auth.dto.KakaoUserInfo;
import java.net.URI;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class KakaoClient {

    private final static String KAKAO_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestClient client;
    private final KakaoProperties properties;

    public KakaoClient(KakaoProperties properties) {
        this.client = RestClient.builder().build();
        this.properties = properties;
    }

    public String getAccessToken(String authorizationCode){
        var body = properties.toBody(authorizationCode);

        ResponseEntity<KakaoAccessToken> response = client.post()
            .uri(URI.create(KAKAO_URL))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoAccessToken.class);

        return Objects.requireNonNull(response.getBody(), "reponse가 null 값입니다.").accessToken();
    }

    public KakaoUserInfo getUserEmail(String accessToken){
        return client.get()
            .uri(KAKAO_USER_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .toEntity(KakaoUserInfo.class)
            .getBody();
    }

    public KakaoProperties getProperties(){
        return properties;
    }
}
