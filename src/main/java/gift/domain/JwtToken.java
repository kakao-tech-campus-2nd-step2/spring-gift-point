package gift.domain;

import gift.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtToken {

    @Value("${jwt.secret.key}")
    private  String secretKey;

    private final Long tokenExpTime = 36000000L;

    public String createToken(Member member) {
        return Jwts.builder()
            .setSubject(member.getEmail())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + tokenExpTime))
            .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
            .compact();
    }

//    public String tokenToEmail(String token) {
//        return Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
//            .getSubject();
//    }
    public Claims extractClaims(String token) {
    if (token.startsWith("Bearer ")) {
        token = token.substring(7);
    }
    return Jwts.parserBuilder()
        .setSigningKey(secretKey.getBytes())
        .build()
        .parseClaimsJws(token)
        .getBody();
    }

    public String tokenToEmail(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }


}
