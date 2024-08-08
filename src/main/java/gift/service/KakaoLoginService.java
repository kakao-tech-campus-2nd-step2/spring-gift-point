package gift.service;

import gift.domain.member.kakao.KakaoTokenRequest;
import gift.domain.member.kakao.KakaoTokenResponse;
import gift.domain.member.kakao.KakaoUserInfoResponse;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class KakaoLoginService {

    private final RestClient restClient;
    private final MemberService memberService;

    @Value("${kakao.client-id}")
    private String clientId;
    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    public KakaoLoginService(RestClient restClient, MemberService memberService) {
        this.restClient = restClient;
        this.memberService = memberService;
    }

    public String getLoginUrl() {
        final String kakaoOauthPageUri = "https://kauth.kakao.com/oauth/authorize?response_type=code";

        return kakaoOauthPageUri + "&redirect_uri=" + redirectUri + "&client_id=" + clientId;
    }

    public String getToken(String code) {
        final String kakaoOauthTokenApiUri = "https://kauth.kakao.com/oauth/token";
        KakaoTokenRequest kakaoTokenRequest = new KakaoTokenRequest("authorization_code", clientId,
            redirectUri, code);

        return Objects.requireNonNull(restClient
                .post()
                .uri(kakaoOauthTokenApiUri)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .body(kakaoTokenRequest.toMap())
                .retrieve()
                .body(KakaoTokenResponse.class))
            .accessToken();
    }

    public Long getKakaoId(String accessToken) {
        final String kakaoUserInfoApiUri = "https://kapi.kakao.com/v2/user/me";

        return Objects.requireNonNull(restClient
                .post()
                .uri(kakaoUserInfoApiUri)
                .header("Authorization", "Bearer " + accessToken)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .body(KakaoUserInfoResponse.class))
            .id();
    }
}
