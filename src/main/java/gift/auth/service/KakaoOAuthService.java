package gift.auth.service;

import gift.auth.persistence.OAuthAccessTokenRepository;
import gift.auth.persistence.OAuthRefreshTokenRepository;
import gift.auth.service.dto.KakaoCallbackInfo;
import gift.auth.service.dto.KakaoTokenResponse;
import gift.auth.service.dto.KakaoUserInfoResponse;
import gift.config.KakaoOauthConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class KakaoOAuthService {
    private final KakaoOauthConfig kakaoOauthConfig;
    private final OAuthAccessTokenRepository oAuthAccessTokenRepository;
    private final OAuthRefreshTokenRepository oAuthRefreshTokenRepository;

    public KakaoOAuthService(KakaoOauthConfig kakaoOauthConfig, OAuthAccessTokenRepository oAuthAccessTokenRepository,
                             OAuthRefreshTokenRepository OAuthRefreshTokenRepository) {
        this.kakaoOauthConfig = kakaoOauthConfig;
        this.oAuthAccessTokenRepository = oAuthAccessTokenRepository;
        this.oAuthRefreshTokenRepository = OAuthRefreshTokenRepository;
    }

    public String getKakaoLoginUrl() {
        return kakaoOauthConfig.createLoginUrl();
    }

    public KakaoCallbackInfo callBack(final String code) {
        var kakaoTokenResponse = getKakaoTokenResponse(code);
        var userInfo = getKakaoUserInfoResponse(kakaoTokenResponse.getAccessTokenWithTokenType());

        var accessToken = kakaoTokenResponse.toAccessTokenFrom(userInfo.kakaoAccount().email());
        var refreshToken = kakaoTokenResponse.toRefreshTokenFrom(userInfo.kakaoAccount().email());

        oAuthAccessTokenRepository.save(accessToken);
        oAuthRefreshTokenRepository.save(refreshToken);

        return KakaoCallbackInfo.of(userInfo.kakaoAccount().email());
    }

    public String getAccessToken(final String username) {
        var token = oAuthAccessTokenRepository.findById(username).orElseThrow();

        return token.getToken();
    }

    private KakaoTokenResponse getKakaoTokenResponse(final String code) {
        var client = kakaoOauthConfig.createTokenClient();

        ResponseEntity<KakaoTokenResponse> response = client.post()
                .body(kakaoOauthConfig.createBody(code))
                .retrieve()
                .toEntity(KakaoTokenResponse.class);

        return response.getBody();
    }

    private KakaoUserInfoResponse getKakaoUserInfoResponse(final String accessToken) {
        var userInfoClient = kakaoOauthConfig.createUserInfoClient();

        var userInfo = userInfoClient.get()
                .header("Authorization", accessToken)
                .retrieve()
                .toEntity(KakaoUserInfoResponse.class);

        return userInfo.getBody();
    }
}