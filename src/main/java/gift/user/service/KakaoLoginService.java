package gift.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gift.config.KakaoProperties;
import gift.user.model.dto.KakaoTokenResponse;
import gift.user.model.dto.KakaoUserInfoResponse;
import java.net.URI;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final RestClient client = RestClient.builder().build();
    private final KakaoProperties kakaoProperties;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public KakaoLoginService(KakaoProperties kakaoProperties) {
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