package gift.util;

import gift.common.properties.KakaoProperties;
import gift.dto.OAuth.AuthTokenInfoResponse;
import gift.dto.OAuth.AuthTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Map;

@Component
public class KakaoApiCaller {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    @Autowired
    public KakaoApiCaller(KakaoProperties kakaoProperties, RestClient restClient) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = restClient;
    }

    public String createGetCodeUrl() {
        String authUrl = kakaoProperties.authUrl();

        String url = UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("client_id", kakaoProperties.restAPiKey())
                .queryParam("redirect_uri", kakaoProperties.redirectUri())
                .queryParam("response_type", "code")
                .toUriString();
        return url;
    }


    public AuthTokenResponse getAccessToken(String authCode) {
        String url = kakaoProperties.tokenUrl();
        MultiValueMap<String, String> params = createParamsForAccessToken(authCode);
        AuthTokenResponse resp = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(AuthTokenResponse.class);

        return resp;
    }

    public AuthTokenResponse refreshAccessToken(String refreshToken) {
        String url = kakaoProperties.tokenUrl();
        MultiValueMap<String, String> params = createParamsForRefreshToken(refreshToken);
        AuthTokenResponse resp = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(params)
                .retrieve()
                .body(AuthTokenResponse.class);

        return resp;
    }

    public String extractUserEmail(String accessToken) {
        String url = kakaoProperties.userInfoUrl();
        Map resp = restClient.get()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(Map.class);
        Map<String, Object> accountMap = (Map<String, Object>) resp.get("kakao_account");
        return (String) accountMap.get("email");
    }

    public String sendMessage(String accessToken, String text) {
        String url = kakaoProperties.sendMessageUrl();

        String templateObject = String.format(
                "{\"object_type\": \"text\", \"text\": \"%s\", \"link\": {\"web_url\": \"https://www.test.com\", \"mobile_web_url\": \"https://www.test.com\"}, \"button_title\": \"선물 확인\"}",
                text
        );
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("template_object", templateObject);

        String resp = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "Bearer " + accessToken)
                .body(formData)
                .retrieve()
                .body(String.class);

        return resp;
    }

    public AuthTokenInfoResponse getTokenInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v1/user/access_token_info";
        AuthTokenInfoResponse resp = restClient.get()
                .uri(URI.create(url))
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(AuthTokenInfoResponse.class);

        return resp;
    }

    private MultiValueMap<String, String> createParamsForAccessToken(String authCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakaoProperties.restAPiKey());
        params.add("redirect_uri", kakaoProperties.redirectUri());
        params.add("code", authCode);
        return params;
    }

    private MultiValueMap<String, String> createParamsForRefreshToken(String refreshToken) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "refresh_token");
        params.add("client_id", kakaoProperties.restAPiKey());
        params.add("redirect_uri", kakaoProperties.redirectUri());
        params.add("refresh_token", refreshToken);
        return params;
    }


}

