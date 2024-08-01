package gift.domain;

import gift.dto.KakaoTokenResponse;
import gift.dto.KakaoUserInfo;
import gift.entity.Member;
import gift.exceptionHandler.RestClientErrorHandler;
import gift.properties.KakaoProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;

@Component
public class KakaoLogin {

    private final KakaoProperties kakaoProperties;
    private final RestClient restClient = RestClient.create();

    public KakaoLogin(KakaoProperties kakaoProperties) {
        this.kakaoProperties = kakaoProperties;
    }


    public KakaoUserInfo getUserInfo(KakaoTokenResponse response) {
        return restClient.post()
            .uri(kakaoProperties.getUserInfoUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .header("Authorization", "Bearer " + response.getAccessToken())
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, RestClientErrorHandler.http4xxErrorHandler)
            .onStatus(HttpStatusCode::is5xxServerError, RestClientErrorHandler.http5xxErrorHandler)
            .body(KakaoUserInfo.class);
    }

    public KakaoTokenResponse getKakaoToken(String code) {
        return restClient.post()
            .uri(kakaoProperties.getTokenUrl())
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(generateGetTokenRequestBody(code))
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError, RestClientErrorHandler.http4xxErrorHandler)
            .onStatus(HttpStatusCode::is5xxServerError, RestClientErrorHandler.http5xxErrorHandler)
            .body(KakaoTokenResponse.class);
    }

    public Member generateMemberByKakaoUserInfo(KakaoUserInfo kakaoUserInfo) {
        return new Member(
            kakaoUserInfo.getId() + "@kakao.com",
            Long.toString(kakaoUserInfo.getId())
        );
    }

    private LinkedMultiValueMap<String, String> generateGetTokenRequestBody(String code) {
        LinkedMultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", kakaoProperties.getClientId());
        body.add("redirect_uri", kakaoProperties.getRedirectUrl());
        body.add("code", code);

        return body;
    }
}
