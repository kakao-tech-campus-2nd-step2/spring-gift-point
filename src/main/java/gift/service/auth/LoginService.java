package gift.service.auth;

import gift.util.auth.JwtToken;
import gift.domain.auth.JWTParameter;
import gift.domain.auth.KakaoToken.kakaoInfo;
import gift.domain.auth.KakaoToken.kakaoToken;
import gift.domain.auth.Login;
import gift.domain.auth.Token;
import gift.entity.auth.UserEntity;
import gift.entity.enums.SocialType;
import gift.repository.auth.SocialRepository;
import gift.repository.auth.UserRepository;
import gift.util.api.ApiCall;
import gift.util.auth.PasswordCrypto;
import gift.util.errorException.BaseHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final SocialRepository socialRepository;
    private final JwtToken jwtToken;
    private final PasswordCrypto passwordCrypto;
    private final ApiCall apiCall;

    @Autowired
    public LoginService(UserRepository userRepository,
        SocialRepository socialRepository, JwtToken jwtToken,
        PasswordCrypto passwordCrypto, ApiCall apiCall) {
        this.userRepository = userRepository;
        this.socialRepository = socialRepository;
        this.jwtToken = jwtToken;
        this.passwordCrypto = passwordCrypto;
        this.apiCall = apiCall;
    }

    public Token login(Login login) {
        UserEntity user = userRepository.findByEmailAndIsDelete(login.getEmail(), 0).orElseThrow(
            () -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
        );

        if (!passwordCrypto.matchesPassword(login.getPassword(), user.getPassword())) {
            throw new BaseHandler(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
        return jwtToken.createToken(new JWTParameter(user.getId(), user.getEmail()));
    }

    public Token kakaoLogin(kakaoToken token) {
        //        토큰, type 으로 유저 조회
        kakaoInfo info = apiCall.getKakaoTokenInfo(token.getAccess_token());
        UserEntity user = socialRepository.findBySocialIdAndType(info.getId(), SocialType.KAKAO)
            .orElseThrow(
                () -> new BaseHandler(HttpStatus.NOT_FOUND, "유저가 존재하지 않습니다.")
            ).getUserEntity();

        return jwtToken.createToken(
            new JWTParameter(user.getId(), user.getEmail(), token.getAccess_token(), SocialType.KAKAO,
                token.getExpires_in()));
    }
}
