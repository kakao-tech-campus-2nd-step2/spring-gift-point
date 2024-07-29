package gift.service;

import gift.response.oauth2.OAuth2TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;

public interface OAuth2LoginService {

    void checkRedirectUriParams(HttpServletRequest request);

    OAuth2TokenResponse getToken(String code);

    String getMemberInfo(String accessToken);

    void saveAccessToken(Long memberId, String accessToken);

    LinkedMultiValueMap<String, String> createTokenRequest(String clientId,
        String redirectUri, String code);
}
