package gift.client;

import static gift.util.constants.auth.KakaoOAuthConstants.KAKAO_SCOPES_URL;
import static gift.util.constants.auth.KakaoOAuthConstants.KAKAO_TOKEN_URL;
import static gift.util.constants.auth.KakaoOAuthConstants.KAKAO_UNLINK_URL;
import static gift.util.constants.auth.KakaoOAuthConstants.KAKAO_USER_INFO_URL;
import static gift.util.constants.auth.KakaoOAuthConstants.SCOPES_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.TOKEN_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.TOKEN_RESPONSE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.UNLINK_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.UNLINK_RESPONSE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.USERINFO_FAILURE_ERROR;
import static gift.util.constants.auth.KakaoOAuthConstants.USERINFO_RESPONSE_ERROR;

import gift.config.KakaoProperties;
import gift.dto.oauth.KakaoScopeResponse;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUnlinkResponse;
import gift.dto.oauth.KakaoUserResponse;
import gift.exception.oauth.KakaoScopeException;
import gift.exception.oauth.KakaoTokenException;
import gift.exception.oauth.KakaoUnlinkException;
import gift.exception.oauth.KakaoUserInfoException;
import java.net.URI;
import java.util.Map;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

@Component
public class KakaoApiClient {

    private final RestClient client;
    private final KakaoProperties kakaoProperties;

    public KakaoApiClient(RestClient client, KakaoProperties kakaoProperties) {
        this.client = client;
        this.kakaoProperties = kakaoProperties;
    }

    public KakaoTokenResponse getAccessToken(String code) {
        LinkedMultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();

        requestBody.add("grant_type", "authorization_code");
        requestBody.add("client_id", kakaoProperties.clientId());
        requestBody.add("redirect_uri", kakaoProperties.redirectUrl());
        requestBody.add("code", code);

        try {
            ResponseEntity<Map<String, Object>> response = client.post()
                .uri(URI.create(KAKAO_TOKEN_URL))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(requestBody)
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                throw new KakaoTokenException(TOKEN_RESPONSE_ERROR);
            }

            String accessToken = (String) responseBody.get("access_token");
            Integer expiresIn = (Integer) responseBody.get("expires_in");
            String refreshToken = (String) responseBody.get("refresh_token");
            Integer refreshTokenExpiresIn = (Integer) responseBody.get("refresh_token_expires_in");

            return new KakaoTokenResponse(accessToken, expiresIn, refreshToken, refreshTokenExpiresIn);

        } catch (RestClientResponseException e) {
            throw new KakaoTokenException(TOKEN_FAILURE_ERROR);
        }
    }

    public KakaoUnlinkResponse unlinkUser(String accessToken) {
        try {
            ResponseEntity<Map<String, Object>> response = client.post()
                .uri(URI.create(KAKAO_UNLINK_URL))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                throw new KakaoUnlinkException(UNLINK_RESPONSE_ERROR);
            }

            Long id = ((Number) responseBody.get("id")).longValue();
            return new KakaoUnlinkResponse(id);

        } catch (RestClientResponseException e) {
            throw new KakaoUnlinkException(UNLINK_FAILURE_ERROR);
        }
    }

    public KakaoScopeResponse getUserScopes(String accessToken) {
        try {
            ResponseEntity<KakaoScopeResponse> response = client.get()
                .uri(URI.create(KAKAO_SCOPES_URL))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(KakaoScopeResponse.class);

            return response.getBody();
        } catch (RestClientResponseException e) {
            throw new KakaoScopeException(SCOPES_FAILURE_ERROR);
        }
    }

    public KakaoUserResponse getUserInfo(String accessToken) {
        try {
            ResponseEntity<Map<String, Object>> response = client.get()
                .uri(URI.create(KAKAO_USER_INFO_URL))
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .toEntity(new ParameterizedTypeReference<Map<String, Object>>() {
                });
            Map<String, Object> responseBody = response.getBody();

            if (responseBody == null) {
                throw new KakaoUserInfoException(USERINFO_RESPONSE_ERROR);
            }

            Long id = ((Number) responseBody.get("id")).longValue();
            Map<String, Object> kakaoAccount = (Map<String, Object>) responseBody.get("kakao_account");
            String nickname = (String) ((Map<String, Object>) kakaoAccount.get("profile")).get("nickname");
            String email = (String) kakaoAccount.get("email");

            return new KakaoUserResponse(id, nickname, email);

        } catch (RestClientResponseException e) {
            throw new KakaoUserInfoException(USERINFO_FAILURE_ERROR);
        }
    }
}
