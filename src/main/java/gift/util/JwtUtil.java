package gift.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secretKey;
  @Value("${jwt.expiration}")
  private Long expiration;
  private Key key;
  private JwtParser jwtParser;

  @PostConstruct
  public void init() {
    byte[] keyBytes = new byte[64];
    new java.security.SecureRandom().nextBytes(keyBytes);
    this.key = Keys.hmacShaKeyFor(keyBytes);
  }

  public String generateToken(String kakaoAccessToken) {
    return Jwts.builder()
            .setSubject(kakaoAccessToken)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
  }

  public String generateToken(Long memberId, String email) {
    return Jwts.builder()
            .setSubject(memberId.toString())
            .claim("email", email)
            .signWith(key, SignatureAlgorithm.HS512)
            .compact();
  }

  public Long getMemberIdFromToken(String token) {
    Claims claims = jwtParser.parseClaimsJws(token).getBody();
    return Long.parseLong(claims.getSubject());
  }
}