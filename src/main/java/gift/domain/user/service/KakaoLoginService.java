package gift.domain.user.service;

import gift.auth.jwt.JwtProvider;
import gift.auth.jwt.JwtToken;
import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import gift.domain.user.repository.OauthTokenJpaRepository;
import gift.domain.user.repository.UserJpaRepository;
import gift.exception.InvalidUserInfoException;
import gift.external.api.kakao.dto.KakaoToken;
import gift.external.api.kakao.dto.KakaoUserInfo;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class KakaoLoginService {

    private final OauthApiProvider<KakaoToken, KakaoUserInfo> kakaoApiProvider; // 좀 이상한 것 같다.. 생각해보기
    private final UserJpaRepository userJpaRepository;
    private final OauthTokenJpaRepository oauthTokenJpaRepository;
    private final JwtProvider jwtProvider;

    public KakaoLoginService(
        OauthApiProvider<KakaoToken, KakaoUserInfo> kakaoApiProvider,
        UserJpaRepository userJpaRepository,
        OauthTokenJpaRepository oauthTokenJpaRepository,
        JwtProvider jwtProvider
    ) {
        this.kakaoApiProvider = kakaoApiProvider;
        this.userJpaRepository = userJpaRepository;
        this.oauthTokenJpaRepository = oauthTokenJpaRepository;
        this.jwtProvider = jwtProvider;
    }

    public String getAuthCodeUrl() {
        return kakaoApiProvider.getAuthCodeUrl();
    }

    @Transactional
    public JwtToken login(String code) {
        KakaoToken kakaoToken = kakaoApiProvider.getToken(code);
        String accessToken = kakaoToken.accessToken();
        String refreshToken = kakaoToken.refreshToken();

        kakaoApiProvider.validateAccessToken(accessToken);
        KakaoUserInfo userInfo = kakaoApiProvider.getUserInfo(accessToken);

        String email = userInfo.kakaoAccount().email();
        String name = userInfo.kakaoAccount().profile().nickname();

        return userJpaRepository.findByEmail(email)
            .map(user -> {
                if (user.getAuthProvider() != AuthProvider.KAKAO) {
                    throw new InvalidUserInfoException("error.invalid.userinfo.provider");
                }
                oauthTokenJpaRepository.save(new OauthToken(null, user, AuthProvider.KAKAO, accessToken, refreshToken));
                return jwtProvider.generateToken(user);
            })
            .orElseGet(() -> signUp(name, email, accessToken, refreshToken));
    }

    private JwtToken signUp(String name, String email, String accessToken, String refreshToken) {
        User user = new User(null, name, email, "kakao", Role.USER, AuthProvider.KAKAO);
        User savedUser = userJpaRepository.save(user);

        oauthTokenJpaRepository.save(new OauthToken(null, savedUser, AuthProvider.KAKAO, accessToken, refreshToken));

        return jwtProvider.generateToken(savedUser);
    }
}
