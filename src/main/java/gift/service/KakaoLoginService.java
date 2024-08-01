package gift.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.dto.oauth.KakaoTokenResponse;
import gift.dto.oauth.KakaoUserInfoResponse;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final RestClient client;
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoLoginService(RestClient client, KakaoProperties kakaoProperties) {
        this.client = client;
        this.kakaoProperties = kakaoProperties;
    }

    public String buildAuthorizeUrl() {
        return "https://kauth.kakao.com/oauth/authorize?" +
                "client_id=" + kakaoProperties.clientId() +
                "&redirect_uri=" + kakaoProperties.redirectUri() +
                "&response_type=code";
    }

    public KakaoTokenResponse getAccessToken(String code) {
        String url = "https://kauth.kakao.com/oauth/token";
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();

        multiValueMap.add("grant_type", "authorization_code");
        multiValueMap.add("redirect_uri", kakaoProperties.redirectUri());
        multiValueMap.add("client_id", kakaoProperties.clientId());
        multiValueMap.add("code", code);

        return client
                .post()
                .uri(URI.create(url))
                .body(multiValueMap)
                .retrieve()
                .body(KakaoTokenResponse.class);
    }

    public String getUserInfo(String accessToken) {
        String url = "https://kapi.kakao.com/v2/user/me";
        String header = "Bearer " + accessToken;

        KakaoUserInfoResponse body = client
                .get()
                .uri(URI.create(url))
                .header("Authorization", header)
                .retrieve()
                .body(KakaoUserInfoResponse.class);

        return body.kakaoAccount().email();
    }
}