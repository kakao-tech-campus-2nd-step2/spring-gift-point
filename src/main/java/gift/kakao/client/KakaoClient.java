package gift.kakao.client;

import gift.kakao.message.dto.KakaoMessageResponse;
import gift.kakao.auth.dto.KakaoTokenResponse;
import gift.kakao.auth.dto.KakaoUserInfoResponse;
import gift.kakao.message.dto.TemplateObject;
import gift.kakao.vo.KakaoProperties;
import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoClient {

    private final RestTemplate restTemplate;
    private final KakaoProperties kakaoProperties;

    private static final String KAKAO_TOKEN_PATH = "/oauth/token";
    private static final String KAKAO_USER_INFO_PATH = "/v2/user/me";
    private static final String KAKAO_MESSAGE_PATH = "/v2/api/talk/memo/default/send";
    private static final String TEXT_OBJECT_TYPE = "text";


    public KakaoClient(RestTemplate restTemplate,
                       KakaoProperties kakaoProperties) {
        this.restTemplate = restTemplate;
        this.kakaoProperties = kakaoProperties;
    }

    public KakaoTokenResponse getTokenResponse(String authCode) {
        String url = kakaoProperties.authDomainName() + KAKAO_TOKEN_PATH;
        RequestEntity<LinkedMultiValueMap<String, String>> request = RequestEntity.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(kakaoProperties.getAuthRequestBody(authCode));

        KakaoTokenResponse response = restTemplate.postForObject(
                url,
                request,
                KakaoTokenResponse.class
        );

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response;
    }

    public Long getUserId(String token) {
        String url = kakaoProperties.apiDomainName() + KAKAO_USER_INFO_PATH;
        RequestEntity<Void> request = RequestEntity.get(url)
                .header(HttpHeaders.AUTHORIZATION, kakaoProperties.authorizationPrefix() + token)
                .build();

        KakaoUserInfoResponse response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                KakaoUserInfoResponse.class
        ).getBody();

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response.id();
    }

    public KakaoTokenResponse getRefreshTokenResponse(String refreshToken) {
        String url = kakaoProperties.authDomainName() + KAKAO_TOKEN_PATH;
        RequestEntity<LinkedMultiValueMap<String, String>> request = RequestEntity.post(url)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(kakaoProperties.getRefreshRequestBody(refreshToken));

        KakaoTokenResponse response = restTemplate.postForObject(
                url,
                request,
                KakaoTokenResponse.class
        );

        if (response == null) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }

        return response;
    }

    public void sendMessageToMe(String token,
                                String text,
                                String path) {
        String apiUrl = kakaoProperties.apiDomainName() + KAKAO_MESSAGE_PATH;
        TemplateObject templateObject = new TemplateObject(
                TEXT_OBJECT_TYPE,
                text,
                kakaoProperties.baseDomainName() + path
        );

        RequestEntity<LinkedMultiValueMap<String, String>> request = RequestEntity.post(apiUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header(HttpHeaders.AUTHORIZATION, kakaoProperties.authorizationPrefix() + token)
                .body(templateObject.toRequestBody());

        KakaoMessageResponse response = restTemplate.postForObject(
                apiUrl,
                request,
                KakaoMessageResponse.class
        );

        if (response == null || !response.isSuccessful()) {
            throw new CustomException(ErrorCode.EXTERNAL_API_UNAVAILABLE);
        }
    }

}
