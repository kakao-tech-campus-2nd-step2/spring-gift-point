package gift.security;

import gift.user.entity.UserRole;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Component
public class JWTUtil {
  private final SecretKey secretKey;

  public JWTUtil(@Value("${jwt.secret}") String secret) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String getEmail(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build()
        .parseClaimsJws(token).getBody().get("email", String.class);
  }

  public UserRole getRole(String token) {
    return UserRole.valueOf(Jwts.parserBuilder().setSigningKey(secretKey).build()
        .parseClaimsJws(token).getBody().get("role", String.class));
  }

  public Boolean isExpired(String token) {
    try {
      return Jwts.parserBuilder().setSigningKey(secretKey).build()
          .parseClaimsJws(token).getBody().getExpiration().before(new Date());
    } catch (ExpiredJwtException e) {
      return true;
    }
  }

  public String createToken(String email, UserRole role, Long expiredMs) {
    return Jwts.builder()
        .claim("email", email)
        .claim("role", role.name())
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setExpiration(new Date(System.currentTimeMillis() + expiredMs))
        .signWith(secretKey)
        .compact();
  }

  public Claims extractAllClaims(String token) {
    return Jwts.parserBuilder().setSigningKey(secretKey).build()
        .parseClaimsJws(token).getBody();
  }

  public List<GrantedAuthority> getAuthorities(Claims claims) {
    UserRole role = UserRole.valueOf(claims.get("role", String.class));
    return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
  }

  public Boolean validateToken(String token, UserDetails userDetails) {
    final String email = getEmail(token);
    return (email.equals(userDetails.getUsername()) && !isExpired(token));
  }
}