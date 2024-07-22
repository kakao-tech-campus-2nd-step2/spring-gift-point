package gift.global.security;

import gift.auth.domain.AuthInfo;
import gift.member.domain.MemberType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class JwtTokenProvider implements TokenManager {

    private final Key signingKey;

    private final long validityInMilliseconds;
    private final long refreshTokenValidityMilliseconds;

    public JwtTokenProvider(@Value("${security.jwt.token.secret}") String secretKey,
                            @Value("${security.jwt.token.expire-length.access}") long validityInMilliseconds,
                            @Value("${security.jwt.token.expire-length.refresh}") long refreshTokenValidityMilliseconds) {

        // 문자열에서 byte로 변환해, Keys의 hmacShaKeyFor 함수를 이용해 Key 자료형의 형태로 byte를 변환
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
        this.validityInMilliseconds = validityInMilliseconds;
        this.refreshTokenValidityMilliseconds = refreshTokenValidityMilliseconds;
    }

    @Override
    public String createAccessToken(AuthInfo authInfo) {
        LocalDateTime now = LocalDateTime.now();
        long issueEpoch = now.atZone(ZoneId.systemDefault()).toEpochSecond();

        // plus millis 가  없어서, 밀리초를 나노초로 변환하여 추가
        LocalDateTime expiryTime = now.plusNanos(validityInMilliseconds * 1_000_000);
        long expiryEpoch = expiryTime.atZone(ZoneId.systemDefault()).toEpochSecond();

        return Jwts.builder()
                .claim("member_id", authInfo.memberId())
                .claim("member_type", authInfo.memberType().getValue())
                .claim("iat", issueEpoch)
                .claim("exp", expiryEpoch)
                .signWith(signingKey)
                .compact();
    }

    @Override
    public AuthInfo getParsedClaims(String token) throws JwtException {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Long memberId = claims.get("member_id", Long.class);
        MemberType memberType = MemberType.valueOf(claims.get("member_type", String.class));
        return new AuthInfo(memberId, memberType);
    }
}
