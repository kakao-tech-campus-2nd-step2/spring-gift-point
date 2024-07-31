package gift.global.util;

import gift.domain.entity.Member;
import gift.domain.exception.forbidden.TokenExpiredException;
import gift.domain.exception.unauthorized.TokenNotFoundException;
import gift.domain.exception.forbidden.TokenStringInvalidException;
import gift.domain.exception.unauthorized.TokenUnexpectedErrorException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * User를 기반으로 token을 생성 및 검증하는 유틸 클래스
 */
@Component
@SuppressWarnings("deprecation")
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private Key key;
    @Value("${jwt.tokenPrefix}")
    private String tokenPrefix;
    private final Logger log = LoggerFactory.getLogger(JwtUtil.class);

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) //1시간
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            // 토큰이 만료됨
            log.warn("Token '{}' has been expired.", token);
            throw new TokenExpiredException();
        } catch (JwtException e) {
            // 알수 없는 Jwt 예외 발생
            log.warn("Token '{}' was invalid.", token);
            throw new TokenUnexpectedErrorException();
        }
    }

    private void checkPrefixOrThrow(String authorizationHeader) {
        if (authorizationHeader == null) {
            log.warn("authorizationHeader was null.");
            throw new TokenNotFoundException();
        }
        if (!authorizationHeader.startsWith(tokenPrefix + " ")) {
            log.warn("header/Authorization's value was not starts with '{}'.", tokenPrefix);
            throw new TokenStringInvalidException();
        }
    }

    private String extractTokenFrom(String authorizationHeader) {
        try {
            return authorizationHeader.substring(tokenPrefix.length() + 1);
        } catch (NullPointerException e) {
            throw new TokenNotFoundException();
        } catch (ArrayIndexOutOfBoundsException e) {
            log.warn("authorizationHeader.substring(begin) indexes was out of bounds. "
                    + "begin was {}, authorizationHeader.length() was {}.", tokenPrefix.length() + 1,
                authorizationHeader.length());
            throw new TokenStringInvalidException();
        }
    }

    public String getSubject(String authorizationHeader) {
        checkPrefixOrThrow(authorizationHeader);
        String token = extractTokenFrom(authorizationHeader);
        return parseClaims(token).getSubject();
    }
}
