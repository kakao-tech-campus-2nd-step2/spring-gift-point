package gift.util;

import gift.domain.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;

public class JwtUtil {
    private static final String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";
    private static final long expirationTime = 1000 * 60 * 60 * 24; // 24 hours

    public static String generateJwtToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + expirationTime);

        String accessToken = Jwts.builder()
            .setSubject(member.getId().toString())
            .setExpiration(expiration)
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();

        return accessToken;
    }

    public static Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();
    }

    public static boolean validateToken(String token) {
        Claims claims = getClaims(token);
        if (claims == null) {
            return false;
        }
        return !claims.getExpiration().before(new Date());
    }
}
