package gift.utils;

import gift.auth.exception.InvalidAccessTokenException;
import gift.member.dto.MemberResDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final String BEARER_PREFIX = "Bearer ";
    public static final int BEARER_PREFIX_LENGTH = BEARER_PREFIX.length();

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(MemberResDto memberResDto, Date expiredAt) {
        Date now = new Date();
        return Jwts.builder()
                .claim("id", memberResDto.id())
                .claim("email", memberResDto.email())
                .issuedAt(now)
                .expiration(expiredAt)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
    }

    public Long getMemberId(String accessToken) {
        return parseClaims(accessToken).get("id", Long.class);
    }

    private Claims parseClaims(String accessToken) {
        return (Claims) Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parse(accessToken)
                .getPayload();
    }

    public String getToken(String authorization) {
        if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
            throw InvalidAccessTokenException.EXCEPTION;
        }

        return authorization.substring(BEARER_PREFIX_LENGTH);
    }

    public boolean validateToken(String authorization) {
        String token = getToken(authorization);

        try {
            return parseClaims(token) != null;
        } catch (Exception e) {
            return false;
        }
    }
}
