package gift.service;

import gift.config.KakaoProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class KakaoAuthProvider {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String GRANT_TYPE = "authorization_code";
    public static final String BEARER_PREFIX = "Bearer ";

    private final KakaoProperties kakaoProperties;

    private final RestTemplate restTemplate;

    public String getTokenRequestUri() {
        return kakaoProperties.getTokenRequestUri();
    }
    public String getMemberInfoRequestUri() {
        return kakaoProperties.getMemberInfoRequestUri();
    }
    public String getRedirectUri() {
        return kakaoProperties.getRedirectUri();
    }
    public String getClientId() {
        return kakaoProperties.getClientId();
    }
    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
