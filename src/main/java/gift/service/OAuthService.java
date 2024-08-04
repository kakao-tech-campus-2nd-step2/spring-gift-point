package gift.service;

import gift.common.auth.JwtTokenProvider;
import gift.common.enums.Role;
import gift.common.enums.SocialType;
import gift.common.exception.ErrorCode;
import gift.common.exception.OAuthException;
import gift.common.util.KakaoUtil;
import gift.controller.oauth.dto.TokenInfoResponse;
import gift.controller.user.dto.UserResponse;
import gift.model.Token;
import gift.model.User;
import gift.repository.TokenRepository;
import gift.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class OAuthService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final KakaoUtil kakaoUtil;

    public OAuthService(UserRepository userRepository, TokenRepository tokenRepository,
        JwtTokenProvider jwtTokenProvider, KakaoUtil kakaoUtil) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.kakaoUtil = kakaoUtil;
    }

    public UserDto register(String code) {
        TokenInfoResponse response = kakaoUtil.getToken(code);
        String accessToken = response.access_token();
        String refreshToken = response.refresh_token();
        Long expiresIn = response.expires_in();
        String email = kakaoUtil.extractEmail(accessToken);

        User user = userRepository.findByEmail(email)
            .orElseGet(() -> userRepository.save(new User("", email, "kakaoUser123", SocialType.KAKAO)));

        if (!user.checkSocialType(SocialType.KAKAO)) {
            throw new OAuthException(ErrorCode.INVALID_SOCIAL_TYPE);
        }

        tokenRepository.save(new Token(accessToken, refreshToken, user, expiresIn));

        String token = jwtTokenProvider.createToken(user.getEmail());

        return UserDto.from(token, user.getName());
    }
}
