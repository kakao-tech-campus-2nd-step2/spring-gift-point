package gift.Token;

import gift.Model.Entity.MemberEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    String secretKey = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    public String createToken(MemberEntity memberEntity) {
        return Jwts.builder()
                .setSubject(memberEntity.getEmail())
                .claim("role", memberEntity.getRole())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public String getEmailFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (SignatureException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.get("role", String.class);
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature role");
            return null;
        }
    }
}
