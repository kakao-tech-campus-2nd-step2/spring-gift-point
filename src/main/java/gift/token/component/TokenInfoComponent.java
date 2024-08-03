package gift.token.component;

import gift.token.model.TokenManager;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

// token으로부터 값을 추출하는 컴포넌트
@Component
public class TokenInfoComponent extends TokenManager {

    // token으로부터 id를 다시 추출
    public long getUserId(String token) {
        String onlyToken = getOnlyToken(token);

        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(onlyToken);

            return Long.parseLong(claims.getBody().getSubject());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.");
        }
    }

    // token으로부터 isAdmin 추출.
    public boolean getIsAdmin(String token) {
        String onlyToken = getOnlyToken(token);

        try {
            Jws<Claims> claims = Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(onlyToken);

            return claims.getBody().get("isAdmin", Boolean.class);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "유효하지 않은 접근입니다.");
        }
    }
}
