package gift.auth.client;

import gift.auth.dto.DefaultTemplateResDto;
import gift.auth.dto.KakaoProperties;
import gift.auth.dto.KakaoTokens;
import gift.auth.dto.KakaoUserInfo;
import gift.auth.dto.TemplateObject;
import java.util.Objects;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoApiClient {

    private final static String KAKAO_API_URL = "https://kauth.kakao.com/oauth/token";
    private final static String KAKAO_USER_INFO_URL = "https://kapi.kakao.com/v2/user/me";
    private final static String KAKAO_MESSAGE_URL = "https://kapi.kakao.com/v2/api/talk/memo/default/send";
    private final static String BASE_URL = "http://localhost:8080";
    private final static String BEARER_PREFIX = "Bearer ";
    private final static String OBJECT_TYPE = "text";

    private final RestClient restClient = RestClient.builder().build();

    private final KakaoProperties kakaoProperties;

    public KakaoApiClient(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }

    public String getAccessToken(String code) {
        LinkedMultiValueMap<String, String> body = kakaoProperties.makeBody(code);

        ResponseEntity<KakaoTokens> entity = restClient.post()
                .uri(KAKAO_API_URL)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(body)
                .retrieve()
                .toEntity(KakaoTokens.class);

        return Objects.requireNonNull(entity.getBody()).accessToken();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return restClient.get()
                .uri(KAKAO_USER_INFO_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntity(KakaoUserInfo.class)
                .getBody();
    }

    public void messageToMe(String accessToken, String text, String path, String buttonTitle) {
        String url = BASE_URL + path;
        TemplateObject templateObject = new TemplateObject(OBJECT_TYPE, text, url, buttonTitle);

        restClient.post()
                .uri(KAKAO_MESSAGE_URL)
                .header(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + accessToken)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(templateObject.makeBody())
                .retrieve()
                .toEntity(DefaultTemplateResDto.class);
    }
}
