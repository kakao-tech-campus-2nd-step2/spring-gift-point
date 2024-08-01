package gift.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;
  private Key key;
  private JwtParser jwtParser;
  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  @PostConstruct
  public void init() {
    key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    jwtParser = Jwts.parser().setSigningKey(key).build();
    key = Keys.hmacShaKeyFor(secretKey.getBytes());
  }

  public String generateToken(Long memberId, String email) {
    return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .signWith(key)
            .compact();
  }

  public Long getMemberIdFromToken(String token) {
    Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
    Claims claims = claimsJws.getBody();
    return Long.parseLong(claims.getSubject());
  }
}
