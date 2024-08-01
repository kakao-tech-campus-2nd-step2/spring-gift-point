package gift.service;

import gift.domain.model.dto.KakaoTokenResponseDto;
import gift.domain.model.dto.KakaoUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

@Service
public class KakaoLoginService {

    private final String clientId;
    private final String redirectUri;
    private final String KAUTH_TOKEN_URL = "https://kauth.kakao.com/oauth/token";
    private final String KAPI_USER_ME_URL = "https://kapi.kakao.com/v2/user/me";
    private final RestClient restClient;

    public KakaoLoginService(@Value("${kakao.client_id}") String clientId,
        @Value("${kakao.redirect_uri}") String redirectUri) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.restClient = RestClient.create();
    }

    public KakaoUserInfo getUserInfo(String accessToken) {
        return restClient.get()
            .uri(KAPI_USER_ME_URL)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
            .retrieve()
            .body(KakaoUserInfo.class);
    }

    public KakaoTokenResponseDto getTokensFromKakao(String code) {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);
        body.add("code", code);

        KakaoTokenResponseDto kakaoTokenResponseDto = restClient.post()
            .uri(KAUTH_TOKEN_URL)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .body(KakaoTokenResponseDto.class);

        if (kakaoTokenResponseDto == null) {
            throw new RuntimeException("Failed to retrieve tokens from Kakao");
        }

        return kakaoTokenResponseDto;
    }
}