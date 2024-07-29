package gift.util;

import gift.dto.OAuth.AuthTokenInfoResponse;
import gift.dto.OAuth.AuthTokenResponse;
import gift.model.token.OAuthToken;
import gift.repository.token.OAuthTokenRepository;
import org.springframework.stereotype.Component;

@Component
public class TokenUtil {

    private final KakaoApiCaller kakaoApiCaller;

    private final OAuthTokenRepository OAuthTokenRepository;

    public TokenUtil(KakaoApiCaller kakaoApiCaller, OAuthTokenRepository OAuthTokenRepository) {
        this.kakaoApiCaller = kakaoApiCaller;
        this.OAuthTokenRepository = OAuthTokenRepository;
    }

    public OAuthToken checkExpiredToken(OAuthToken OAuthToken) {
        AuthTokenInfoResponse tokenInfo = kakaoApiCaller.getTokenInfo(OAuthToken.getAccessToken());
        if (tokenInfo.expiresIn() == 0) {
            AuthTokenResponse tokenResponse = kakaoApiCaller.refreshAccessToken(OAuthToken.getRefreshToken());
            OAuthToken.updateAccessToken(tokenResponse.accessToken());
            OAuthTokenRepository.save(OAuthToken);
            return OAuthToken;
        }
        return OAuthToken;
    }

}
