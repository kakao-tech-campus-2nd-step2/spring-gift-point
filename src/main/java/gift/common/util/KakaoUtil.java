package gift.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gift.common.exception.ErrorCode;
import gift.common.exception.OAuthException;
import gift.common.properties.KakaoProperties;
import gift.controller.oauth.dto.Link;
import gift.controller.oauth.dto.TextTemplate;
import gift.controller.oauth.dto.TokenInfoResponse;
import gift.controller.oauth.dto.UserInfoResponse;
import gift.model.Token;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoUtil {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;
    private final ObjectMapper objectMapper;

    public KakaoUtil(KakaoProperties kakaoProperties, ObjectMapper objectMapper) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = RestClient.builder().build();
        this.objectMapper = objectMapper;
    }

    public String extractEmail(String accessToken) {
        var url = kakaoProperties.userInfoUrl();
        UserInfoResponse response = restClient.get()
            .uri(URI.create(url))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .toEntity(UserInfoResponse.class)
            .getBody();

        String email = response.kakao_account().email();
        return email;
    }

    public TokenInfoResponse getToken(String code, String redirectUrl) {
        var url = kakaoProperties.tokenUrl();
        LinkedMultiValueMap<String, String> body = createAccessBody(code, redirectUrl);

        try {
            TokenInfoResponse response = restClient.post()
                .uri(URI.create(url))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(body)
                .retrieve()
                .toEntity(TokenInfoResponse.class)
                .getBody();

            return response;
        } catch (ResourceAccessException e) {
            throw new OAuthException(ErrorCode.NETWORK_ERROR);
        }
    }

    public String getRequestUrl(String redirectUrl) {
        var kakaoLoginUrl = UriComponentsBuilder.fromHttpUrl(kakaoProperties.authUrl())
            .queryParam("scope", "account_email")
            .queryParam("response_type", "code")
            .queryParam("redirect_uri", redirectUrl)
            .queryParam("client_id", kakaoProperties.clientId())
            .build().toString();
        return kakaoLoginUrl;
    }

    public LinkedMultiValueMap<String, String> createAccessBody(String code, String redirectUrl) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", redirectUrl);
        body.add("code", code);
        return body;
    }

    public LinkedMultiValueMap<String, String> createRefreshBody(String code) {
        var body = new LinkedMultiValueMap<String, String>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_url", kakaoProperties.redirectUri());
        body.add("code", code);
        return body;
    }

    public void sendMessage(String kakaoToken, String message) {
        var url = kakaoProperties.messageUrl();
        TextTemplate textTemplate = new TextTemplate("text", message,
            new Link(kakaoProperties.webUrl()));
        String jsonTemplate = null;
        try {
            jsonTemplate = objectMapper.writeValueAsString(textTemplate);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.set("template_object", jsonTemplate);

        var response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + kakaoToken)
            .body(body)
            .retrieve();
    }

    public TokenInfoResponse refreshAccessToken(String refreshToken) {
        var url = kakaoProperties.refreshUrl();
        LinkedMultiValueMap<String, String> body = createRefreshBody(refreshToken);

        TokenInfoResponse response = restClient.post()
            .uri(URI.create(url))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(TokenInfoResponse.class)
            .getBody();

        return response;
    }

    public void checkExpiredAccessToken(Token token) {
        if (token.isTokenExpired()) {
            TokenInfoResponse response = refreshAccessToken(token.getRefreshToken());
            token.updateToken(response.access_token(), response.refresh_token(), response.expires_in());
        }
    }
}
