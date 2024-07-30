package gift.client;

import gift.dto.response.KakaoTokenResponse;
import gift.dto.response.KakaoUserInfoResponse;
import gift.exception.KakaoApiHasProblemException;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class KakaoApiClient {

    private static final String TOKEN_REQUEST_URI = "https://kauth.kakao.com/oauth/token";
    private static final String USER_INFO_REQUEST_URI = "https://kapi.kakao.com/v2/user/me";
    private static final String MESSAGE_SEND_REQUEST_URI = "https://kapi.kakao.com/v2/api/talk/memo/default/send";

    private final RestClient restClient;

    public KakaoApiClient(RestClient.Builder builder) {
        this.restClient = builder.build();
    }

    public KakaoTokenResponse getKakaoToken(MultiValueMap<String, String> bodyParams) {

        int maxRetries = 4;
        int retryCount = 0;
        List<Exception> exceptions = new ArrayList<>();
        while (retryCount < maxRetries) {
            try {
                return restClient
                        .post()
                        .uri(TOKEN_REQUEST_URI)
                        .body(bodyParams)
                        .retrieve()
                        .onStatus(new RestClientResponseErrorHandler())
                        .body(KakaoTokenResponse.class);
            } catch (Exception e) {
                exceptions.add(e);
                retryCount++;
            }
        }
        throw new KakaoApiHasProblemException("카카오API의 '토큰 받기' 기능에 문제가 생겼습니다.", exceptions);
    }

    public String getMemberEmail(String token) {

        int maxRetries = 4;
        int retryCount = 0;
        List<Exception> exceptions = new ArrayList<>();
        while (retryCount < maxRetries) {
            try {
                return Objects.requireNonNull(restClient
                                .get()
                                .uri(USER_INFO_REQUEST_URI)
                                .header("Authorization", String.format("Bearer %s", token))
                                .retrieve()
                                .onStatus(new RestClientResponseErrorHandler())
                                .body(KakaoUserInfoResponse.class))
                        .kakaoAccount()
                        .email();
            } catch (Exception e) {
                exceptions.add(e);
                retryCount++;
            }
        }
        throw new KakaoApiHasProblemException("카카오API의 '사용자 정보 가져오기' 기능에 문제가 생겼습니다.", exceptions);
    }

    public void sendMessageToMe(String accessToken, MultiValueMap<String, String> bodyParams) {

        int maxRetries = 4;
        int retryCount = 0;
        List<Exception> exceptions = new ArrayList<>();

        while (retryCount < maxRetries) {
            try {
                restClient.post()
                        .uri(MESSAGE_SEND_REQUEST_URI)
                        .body(bodyParams)
                        .header("Authorization", String.format("Bearer %s", accessToken))
                        .retrieve()
                        .onStatus(new RestClientResponseErrorHandler());
                return;
            } catch (Exception e) {
                exceptions.add(e);
                retryCount++;
            }
        }
        throw new KakaoApiHasProblemException("카카오API의 '나에게 메세지 보내기' 기능에 문제가 생겼습니다.", exceptions);
    }
}
