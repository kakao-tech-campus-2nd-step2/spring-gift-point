package gift.auth;

import gift.service.KakaoApiService;
import gift.service.MemberService;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    private final KakaoApiService kakaoApiService;
    private final MemberService memberService;

    // Token 만료 시간
    private final static long TOKEN_TIME = 60 * 60 *1000L; //60분
    private final static String SECRET_KEY = "mysecretmysecretmysecretmysecretmysecretmysecret";
    private final static SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final JwtHelper jwtHelper;

    public JwtUtil(KakaoApiService kakaoApiService, MemberService memberService, JwtHelper jwtHelper) {
        this.kakaoApiService = kakaoApiService;
        this.memberService = memberService;
        this.jwtHelper = jwtHelper;
    }

    private Long getMemberIdFromToken(Token token) {
        return jwtHelper.getClaims(token).get("userId", Long.class);
    }

    /**
     *
     * @param authorizationHeader Authorization 헤더
     * @return Bearer 토큰 추출
     */
    public Token getBearerTokenFromAuthorizationHeader(String authorizationHeader) {
        String bearerToken = authorizationHeader.replace("Bearer ", "");
        return new Token(bearerToken);
    }

    private Long getMemberIdFromKakao(Token token) {
        String memberEmail = kakaoApiService.getMemberEmailFromKakao(token);
        return memberService.getMemberByEmail(memberEmail).getId();
    }

    public Long getMemberIdFromAuthorizationHeader(String authorizationHeader) {
        Token fetchedToken = getBearerTokenFromAuthorizationHeader(authorizationHeader);

        if (jwtHelper.isJwtToken(fetchedToken)) {
            return getMemberIdFromToken(fetchedToken);
        } else {
            return getMemberIdFromKakao(fetchedToken);
        }
    }

    public boolean isNotJwtToken(Token token) {
        return token.token().split("\\.").length != 3;
    }

}
