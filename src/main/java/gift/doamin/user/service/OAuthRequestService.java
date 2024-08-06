package gift.doamin.user.service;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.doamin.user.dto.KakaoOAuthTokenResponse;
import gift.doamin.user.dto.KakaoOAuthUserInfoResponse;
import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.properties.KakaoClientProperties;
import gift.doamin.user.properties.KakaoProviderProperties;
import gift.doamin.user.util.AuthorizationOAuthUriBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OAuthRequestService {

    private final KakaoClientProperties clientProperties;
    private final KakaoProviderProperties providerProperties;

    private final RestClient restClient = RestClient.builder().build();

    public OAuthRequestService(KakaoClientProperties clientProperties,
        KakaoProviderProperties providerProperties) {
        this.clientProperties = clientProperties;
        this.providerProperties = providerProperties;
    }

    public String getAuthUrl() {
        return new AuthorizationOAuthUriBuilder()
            .clientProperties(clientProperties)
            .providerProperties(providerProperties)
            .build();
    }

    // 인가코드로 카카오 oauth 토큰을 발급받는 함수
    public KakaoOAuthTokenResponse requestToken(String authorizeCode) {

        String tokenUri = providerProperties.tokenUri();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizeCode);
        body.add("redirect_uri", clientProperties.redirectUri());
        body.add("client_id", clientProperties.clientId());
        body.add("client_secret", clientProperties.clientSecret());

        ResponseEntity<KakaoOAuthTokenResponse> entity = restClient.post()
            .uri(tokenUri)
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoOAuthTokenResponse.class);

        return entity.getBody();
    }

    // 카카오 서버에서 사용자 정보를 받아오는 함수
    public KakaoOAuthUserInfoResponse requestUserInfo(String kakaoAccessToken) {

        return restClient.get()
            .uri(providerProperties.userInfoUri())
            .header("Authorization", "Bearer " + kakaoAccessToken)
            .header("Content-Type", "application/x-www-form-urlencoded")
            .retrieve()
            .body(KakaoOAuthUserInfoResponse.class);
    }

    @Transactional
    public KakaoOAuthTokenResponse requestRenewTokens(KakaoOAuthToken kakaoOAuthToken) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientProperties.clientId());
        body.add("client_secret", clientProperties.clientSecret());
        body.add("refresh_token", kakaoOAuthToken.getRefreshToken());

        return restClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(KakaoOAuthTokenResponse.class);
    }
}
