package gift.token.component;

import static gift.global.utility.TimeConvertUtil.minuteToMillis;

import gift.token.model.TokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

// JWT 토큰을 생성해주는 bean 클래스.
// 슬랙의 글을 참고하여, util보다는 bean이 적합할 것 같아 bean으로 생성
@Component
public class TokenComponent extends TokenManager {

    public TokenComponent() {
        super();
    }

    // 입력한 정보를 토대로 access 토큰을 반환하는 함수 (클래스끼리의 이동이므로 dto로 전달)
    public String getToken(Long id, String platformUniqueId, boolean isAdmin) {
        long currentTime = System.currentTimeMillis();
        // 유효기간은 30분
        long expirationTime = minuteToMillis(30);
        Date currentDate = new Date(currentTime);
        Date expirationDate = new Date(currentTime + expirationTime);

        String onlyAccessToken = Jwts.builder()
            .subject(String.valueOf(id))
            .claim("isAdmin", isAdmin)
            .claim("platformUniqueId", platformUniqueId)
            .issuedAt(currentDate)
            .expiration(expirationDate)
            .signWith(secretKey)
            .compact();

        // 인증 방식 + 토큰으로 반환
        return getFullToken(onlyAccessToken);
    }

    // accessToken을 디코딩해서 유효기간이 지났는지 확인
    public void verifyAccessTokenExpiry(String accessToken) {
        String onlyAccessToken = getOnlyToken(accessToken);
        boolean isExpired = true;

        try {
            Jws<Claims> claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY_STRING.getBytes()))
                .build()
                .parseSignedClaims(onlyAccessToken);

            isExpired = claims
                .getPayload()
                .getExpiration()
                .before(new Date());
        } catch (Exception e) {
            // 서명이 잘못됐거나, 토큰이 잘못됐거나 등등의 이유로 다양한 예외가 발생할 수 있으므로 Exception으로 잡은 후에 401을 반환하겠습니다.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다.");
        }

        if (isExpired) {
            // 해당 문구와 예외를 보면 프런트에서는 refresh token으로 인가 요청을 보내야 함.
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        }
    }

    private String getFullToken(String tokenOnly) {
        return BEARER + tokenOnly;
    }


}
