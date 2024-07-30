package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;

public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000L;

    public static String generateToken(String email) {
        return Jwts.builder()
            .setSubject(email)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(key)
            .compact();
    }

    public static String extractEmail(String token) {
        try {
            String email = getClaims(token).getSubject();
            logger.debug("토큰에서 추출된 이메일: {}", email);
            return email;
        } catch (Exception e) {
            logger.error("토큰에서 이메일 추출 오류", e);
            return null;
        }
    }

    public static boolean validateToken(String token, String email) {
        try {
            String extractedEmail = extractEmail(token);
            boolean isValid = extractedEmail != null && extractedEmail.equals(email) && !isTokenExpired(token);
            logger.debug("토큰 검증 결과: {}", isValid);
            return isValid;
        } catch (Exception e) {
            logger.error("토큰 검증 오류", e);
            return false;
        }
    }

    private static Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    private static boolean isTokenExpired(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }
}
