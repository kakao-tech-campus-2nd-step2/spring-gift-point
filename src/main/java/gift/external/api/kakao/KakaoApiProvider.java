package gift.external.api.kakao;

import com.fasterxml.jackson.databind.JsonNode;
import gift.domain.order.service.MessageApiProvider;
import gift.domain.member.service.OauthApiProvider;
import gift.external.api.kakao.client.KakaoApiClient;
import gift.external.api.kakao.client.KakaoAuthClient;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Component
public class KakaoApiProvider<T, E> implements OauthApiProvider<T, E>, MessageApiProvider<T> {

    private final KakaoProperties kakaoProperties;
    private final KakaoAuthClient kakaoAuthClient;
    private final KakaoApiClient kakaoApiClient;

    private static final String[] SCOPE = { "profile_nickname", "talk_message", "account_email" };
    private static final String RESPONSE_TYPE = "code";

    public KakaoApiProvider(
        KakaoProperties kakaoProperties,
        KakaoAuthClient kakaoAuthClient,
        KakaoApiClient kakaoApiClient
    ) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoAuthClient = kakaoAuthClient;
        this.kakaoApiClient = kakaoApiClient;
    }

    public String getAuthCodeUrl() {
        return kakaoProperties.authBaseUrl()
            + "?scope=" + String.join(",", SCOPE)
            + "&response_type=" + RESPONSE_TYPE
            + "&redirect_uri=" + kakaoProperties.redirectUri();
    }

    public T getToken(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", code);

        return (T) kakaoAuthClient.getAccessToken(body);
    }

    public void validateAccessToken(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        kakaoApiClient.getAccessTokenInfo(headers);
    }

    public E getUserInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return (E) kakaoApiClient.getUserInfo(headers);
    }

    public T renewToken(String refreshToken) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.clientId());
        body.add("refresh_token", refreshToken);

        return (T) kakaoAuthClient.getAccessToken(body);
    }

    public T sendMessageToMe(String accessToken, String templateObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("template_object", templateObject);

        JsonNode jsonResponse = kakaoApiClient.sendMessageToMe(headers, body);
        return (T) jsonResponse.get("result_code").asText();
    }
}
