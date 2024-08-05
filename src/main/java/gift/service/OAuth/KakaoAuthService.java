package gift.service.OAuth;

import gift.common.enums.LoginType;
import gift.common.enums.TokenType;
import gift.dto.OAuth.*;
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

    public String createCodeUrl(String rediectUrl) {
        return kakaoApiCaller.createGetCodeUrl(rediectUrl);
    }

    public LoginInfoResponse.Info register(String authCode, String redirectUrl) {

        AuthTokenResponse tokenResponse = kakaoApiCaller.getAccessToken(authCode, redirectUrl);

        UserInfoResponse.Info userInfo = kakaoApiCaller.extractUserInfo(tokenResponse.accessToken());

        User user = userRepository.findByEmail(userInfo.email()).orElseGet(
                () -> userRepository.save(new User(userInfo.email(), "1234", "testName", LoginType.KAKAO))
        );

        user.checkLoginType(LoginType.KAKAO);

        saveKakaoAccessToken(tokenResponse.accessToken(), tokenResponse.refreshToken(), user);
        return new LoginInfoResponse.Info(userInfo.name(), jwtUtil.generateJWT(user));
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