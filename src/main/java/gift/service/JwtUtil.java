package gift.service;

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

    private Long getMemberIdFromToken(String token) {
        return jwtHelper.getClaims(token).get("userId", Long.class);
    }

    /**
     *
     * @param authorizationHeader Authorization 헤더
     * @return Bearer 토큰 추출
     */
    public String getBearerTokenFromAuthorizationHeader(String authorizationHeader) {
        return authorizationHeader.replace("Bearer ", "");
    }

    private Long getMemberIdFromKakao(String token) {
        String memberEmail = kakaoApiService.getMemberEmailFromKakao(token);
        return memberService.getMemberByEmail(memberEmail).getId();
    }

    public Long getMemberIdFromAuthorizationHeader(String authorizationHeader) {
        String token = getBearerTokenFromAuthorizationHeader(authorizationHeader);

        if (jwtHelper.isJwtToken(token)) {
            return getMemberIdFromToken(token);
        } else {
            return getMemberIdFromKakao(token);
        }
    }

    public boolean isJwtToken(String token) {
        return token.split("\\.").length == 3;
    }

}
