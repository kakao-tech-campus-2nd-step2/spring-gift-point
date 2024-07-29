package gift.application.token;

import gift.application.token.apicaller.KakaoTokenApiCaller;
import gift.model.token.KakaoToken;
import gift.repository.token.TokenRepository;
import org.springframework.stereotype.Component;

@Component
public class TokenManager {

    private final KakaoTokenApiCaller kakaoTokenApiCaller;
    private final TokenRepository tokenRepository;

    public TokenManager(KakaoTokenApiCaller kakaoTokenApiCaller, TokenRepository tokenRepository) {
        this.kakaoTokenApiCaller = kakaoTokenApiCaller;
        this.tokenRepository = tokenRepository;
    }

    /**
     * 인가 코드를 사용해서 토큰 가져오기
     */
    public KakaoToken getTokenByAuthorizationCode(String authorizationCode) {
        KakaoToken token = kakaoTokenApiCaller.getToken(authorizationCode);
        System.out.println("token = " + token.getAccessToken());
        return token;
    }

    public void saveToken(Long userId, KakaoToken token) {
        tokenRepository.saveToken(userId, token);
    }

    public KakaoToken getToken(Long userId) {
        KakaoToken token = tokenRepository.getToken(userId);

        if (token.isValid()) {
            return token;
        }

        KakaoToken newToken = kakaoTokenApiCaller.refreshAccessToken(token.getRefreshToken());
        token.update(newToken);
        tokenRepository.saveToken(userId, token);
        return newToken;
    }
}
