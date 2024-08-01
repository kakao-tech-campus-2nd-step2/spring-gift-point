package gift.auth.service;

import gift.exceptionAdvisor.exceptions.GiftUnauthorizedException;
import gift.member.entity.Member;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Jwts.SIG;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationTool {

    private final SecretKey key = SIG.HS256.key().build();

    public AuthenticationTool() {
    }

    public String makeToken(Member member) {
        if (member.getId() == null){
            throw new NullPointerException("JWT error");
        }
        return Jwts.builder().claim("id", member.getId())
            .signWith(key).compact();
    }

    public long parseToken(String token) {
        try {
            Claims claims = (Claims)Jwts.parser().verifyWith(key).build().parse(token).getPayload();
            return Long.parseLong(claims.get("id").toString());
        } catch (JwtException e) {
            throw new GiftUnauthorizedException("잘못된 토큰입니다.");
        }

    }


}
