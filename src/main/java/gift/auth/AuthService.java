package gift.auth;

import ch.qos.logback.classic.Logger;
import gift.exception.NotFoundMember;
import gift.login.JwtTokenUtil;
import gift.member.Member;
import gift.member.MemberService;
import io.jsonwebtoken.Jwt;
import org.springframework.stereotype.Service;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private final KakaoClient kakaoClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final MemberService memberService;

    public AuthService(KakaoClient kakaoClient, JwtTokenUtil jwtTokenUtil, MemberService memberService) {
        this.kakaoClient = kakaoClient;
        this.jwtTokenUtil = jwtTokenUtil;
        this.memberService = memberService;
    }

    public String getTokenFromAuthorizationCode(String authorizationCode) {
        String accessToken = kakaoClient.getAccessToken(authorizationCode);
        KakaoUserInfo kakaoUserInfo = kakaoClient.getUserInfo(accessToken);

        return JwtTokenUtil.generateToken(kakaoUserInfo.kakaoAccount().email());
    }

    public String login(String token, LoginRequestDto loginRequestDto) throws NotFoundMember {
        String memberEmail = JwtTokenUtil.decodeJwt(token).getSubject();

        Member member = memberService.getMemberByEmail(memberEmail);
        if (member.getEmail().equals(loginRequestDto.email()) && member.getPassword().equals(loginRequestDto.password())) {
            throw new NotFoundMember("회원정보가 올바르지 않습니다");
        }

        return JwtTokenUtil.generateToken(memberEmail);
    }

}
