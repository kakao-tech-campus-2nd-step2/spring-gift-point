package gift.doamin.user.service;

import gift.doamin.user.dto.KakaoOAuthTokenResponse;
import gift.doamin.user.dto.KakaoOAuthUserInfoResponse;
import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.repository.KakaoOAuthTokenRepository;
import gift.doamin.user.repository.UserRepository;
import gift.global.util.JwtDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OAuthService {

    private final UserRepository userRepository;
    private final KakaoOAuthTokenRepository kakaoOAuthTokenRepository;
    private final OAuthRequestService oAuthRequestService;
    private final AuthTokenService authTokenService;

    public OAuthService(UserRepository userRepository,
        KakaoOAuthTokenRepository kakaoOAuthTokenRepository,
        OAuthRequestService oAuthRequestService,
        AuthTokenService authTokenService) {
        this.userRepository = userRepository;
        this.kakaoOAuthTokenRepository = kakaoOAuthTokenRepository;
        this.oAuthRequestService = oAuthRequestService;
        this.authTokenService = authTokenService;
    }

    @Transactional
    public JwtDto login(KakaoOAuthTokenResponse tokenResponse,
        KakaoOAuthUserInfoResponse userInfo) {

        // 우리 서비스에서 사용할 email 및 nickname 생성
        Long id = userInfo.getId();
        String nickname = userInfo.getProperties().get("nickname");
        String email = id + "@kakao.oauth";

        // 유저 정보를 찾아오고, 등록되지 않은 유저면 새로 등록함
        User user = userRepository.findByEmail(email)
            .orElseGet(
                () -> userRepository.save(new User(email, id.toString(), nickname, UserRole.USER))
            );

        // 카카오 OAuth 토큰 저장
        KakaoOAuthToken kakaoOAuthToken = kakaoOAuthTokenRepository.findByUser(user)
            .orElseGet(() -> new KakaoOAuthToken(user));
        kakaoOAuthToken.update(tokenResponse);

        // 우리 서비스의 jwt 토큰 생성해서 반환
        return authTokenService.genrateToken(user);
    }

    @Transactional
    public void renewOAuthTokens(KakaoOAuthToken kakaoOAuthToken) {

        KakaoOAuthTokenResponse tokenResponse = oAuthRequestService.requestRenewTokens(
            kakaoOAuthToken);

        kakaoOAuthToken.update(tokenResponse);
    }

}
