package gift.authorization;

import gift.dto.request.LoginMemberDTO;
import gift.entity.Member;
import gift.entity.MemberType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(Member member) {
        Date now = new Date();
        Date expiration = new Date(System.currentTimeMillis() + 1 * (1000 * 60 * 60 * 24 * 365));
        String accessToken = Jwts.builder()
                .setSubject(member.getEmail())
                .claim("email", member.getEmail())
                .claim("type", member.getType().getValue()) //string
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .compact();
        return accessToken;
    }

    //claims 추출
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public MemberType getMemberType(String token){
        try{
            Claims claims = extractClaims(token);
            if (claims.get("type") == null) {
                throw new JwtException("error.invalid.token.type.null");
            }
            String type = claims.get("type", String.class);
            return MemberType.fromValue(type);
        }catch(JwtException e){
            System.out.println("type null..");
            throw new JwtException("error.invalid.token");
        }
    }

    // email 얻음
    public String getUserEmail(String token) {
        try {
            Claims claims = extractClaims(token);
            if (claims.get("email") == null) {
                throw new JwtException("error.invalid.token.type.null");
            }
            return claims.get("email", String.class);
        } catch (JwtException e) {
            System.out.println("email null..");
            throw new JwtException("error.invalid.token");
        }
    }

    public boolean checkClaim(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey.getBytes())
                    .parseClaimsJws(jwt)
                    .getBody();
            return true;
        }catch(ExpiredJwtException e) {   //Token이 만료된 경우 Exception이 발생한다.
            return false;
        }catch(JwtException e) {        //Token이 변조된 경우 Exception이 발생한다.
            return false;
        }
    }

    public boolean isNotValidToken(LoginMemberDTO loginMemberDTO){
        String token = loginMemberDTO.token();
        System.out.println("token: " + token);
        if(checkClaim(token)){
            return false;
        }
        return true;
    }

}
