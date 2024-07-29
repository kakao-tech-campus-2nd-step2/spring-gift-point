package gift.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

  @Value("${jwt.secret}")
  private String secretKey;

  public String createToken(String email) {
    Date now = new Date();
    long validityInMilliseconds = 3600000;
    Date validity = new Date(now.getTime() + validityInMilliseconds);

    return Jwts.builder().setSubject(email).setIssuedAt(now).setExpiration(validity)
        .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .compact();

  }

  public String getEmailFromToken(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}
