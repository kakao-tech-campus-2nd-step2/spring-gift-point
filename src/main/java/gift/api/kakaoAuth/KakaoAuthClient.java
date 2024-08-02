package gift.api.kakaoAuth;

import gift.api.KakaoProperties;
import java.net.URI;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoAuthClient {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient;

    public KakaoAuthClient(KakaoProperties kakaoProperties, RestClient restClient) {
        this.kakaoProperties = kakaoProperties;
        this.restClient = restClient;
    }

    public URI getAuthorization() {
        return URI.create(kakaoProperties.getAuthorizationUrl());
    }

    public KakaoTokenResponse getKakaoTokenResponse(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = createGetTokenBody(authorizationCode);
        return restClient.post()
            .uri(URI.create(kakaoProperties.tokenUrl()))
            .body(body)
            .retrieve()
            .body(KakaoTokenResponse.class);
    }

    @Retryable(backoff = @Backoff(delay = 1000))
    public String getEmail(String accessToken) {
        KakaoMemberResponse response = restClient.get()
            .uri(URI.create(kakaoProperties.memberUrl()))
            .header("Authorization", "Bearer " + accessToken)
            .retrieve()
            .body(KakaoMemberResponse.class);
        return response.kakaoAccount().email();
    }

    @Retryable(backoff = @Backoff(delay = 1000))
    public KakaoTokenResponse refreshAccessToken(String refreshToken) {
        return restClient.post()
            .uri(URI.create(kakaoProperties.tokenUrl()))
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(createRefreshTokenBody(refreshToken))
            .retrieve()
            .body(KakaoTokenResponse.class);
    }

    private LinkedMultiValueMap<String, String> createGetTokenBody(String authorizationCode) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.clientId());
        body.add("redirect_uri", kakaoProperties.redirectUri());
        body.add("code", authorizationCode);
        return body;
    }

    private LinkedMultiValueMap<String, String> createRefreshTokenBody(String refreshToken) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", kakaoProperties.clientId());
        body.add("refresh_token", refreshToken);
        return body;
    }
}
