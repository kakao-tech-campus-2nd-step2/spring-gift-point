package gift.service.OAuth;

import gift.common.enums.LoginType;
import gift.common.enums.TokenType;
import gift.dto.OAuth.AuthTokenResponse;
import gift.model.token.OAuthToken;
import gift.model.user.User;
import gift.repository.token.OAuthTokenRepository;
import gift.repository.user.UserRepository;
import gift.util.JwtUtil;
import gift.util.KakaoApiCaller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class KakaoAuthService {

    private final KakaoApiCaller kakaoApiCaller;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final OAuthTokenRepository OAuthTokenRepository;

    @Autowired
    public KakaoAuthService(KakaoApiCaller kakaoApiCaller, JwtUtil jwtUtil, UserRepository userRepository, OAuthTokenRepository OAuthTokenRepository) {
        this.kakaoApiCaller = kakaoApiCaller;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.OAuthTokenRepository = OAuthTokenRepository;
    }

    public String createCodeUrl() {
        return kakaoApiCaller.createGetCodeUrl();
    }

    public String register(String authCode) {

        AuthTokenResponse tokenResponse = kakaoApiCaller.getAccessToken(authCode);
        String email = kakaoApiCaller.extractUserEmail(tokenResponse.accessToken());

        User user = userRepository.findByEmail(email).orElseGet(
                () -> userRepository.save(new User(email, "1234", LoginType.KAKAO))
        );

        user.checkLoginType(LoginType.KAKAO);

        saveKakaoAccessToken(tokenResponse.accessToken(), tokenResponse.refreshToken(), user);

        return jwtUtil.generateJWT(user);
    }

    private void saveKakaoAccessToken(String accessToken, String refreshToken, User user) {
        OAuthTokenRepository.findByUser(user).ifPresentOrElse(
                existingToken -> {
                    existingToken.updateTokens(refreshToken, accessToken);
                    OAuthTokenRepository.save(existingToken);
                },
                () -> {
                    OAuthToken newToken = new OAuthToken(user, accessToken, refreshToken, TokenType.KAKAO);
                    OAuthTokenRepository.save(newToken);
                }
        );
    }
}