package gift.Util;

import gift.Exception.InvalidTokenException;
import gift.Exception.TokenExpiredException;
import gift.Exception.NullTokenException;
import gift.Exception.NotValidTokenException;
import gift.Model.Entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String secretKey;
    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes((StandardCharsets.UTF_8)));
    }

    public String generateToken(Member user) {
        return Jwts.builder()
                .setSubject(user.getEmail().getValue())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 3600)) //1시간 동안 토큰 유효
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token) {
        return extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        if (token == null) {
            throw new NullTokenException("null토큰 입니다");
        }
        if (!token.contains("Bearer ")) {
            throw new NotValidTokenException("잘못된 토큰 입니다");
        }
        String tokenValue = token.split(" ")[1];

        try {
            return Jwts.parser()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(tokenValue)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new TokenExpiredException("만료된 토큰 입니다");
        } catch (SignatureException e) {
            throw new InvalidTokenException("올바르지 않은 토큰 입니다");
        }
    }
}