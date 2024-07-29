package gift.auth.service;

import gift.auth.JwtToken;
import gift.auth.domain.JWTParameter;
import gift.auth.domain.KakaoToken.kakaoInfo;
import gift.auth.domain.KakaoToken.kakaoToken;
import gift.auth.domain.Login;
import gift.auth.domain.Token;
import gift.entity.UserEntity;
import gift.entity.enums.SocialType;
import gift.repository.SocialRepository;
import gift.repository.UserRepository;
import gift.util.ApiCall;
import gift.util.PasswordCrypto;
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
