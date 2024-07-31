package gift.authentication.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Optional;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtResolver {

    private final SecretKey key;
    private final String MEMBER_ID_CLAIM_KEY = "memberId";
    private final String SOCIAL_TOKEN_CLAIM_KEY = "socialToken";

    public JwtResolver(@Value("${jwt.secretkey}") String secret) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public Claims resolve(Token token) {
        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token.getValue())
            .getPayload();
    }

    public Optional<Long> resolveId(Token token) {
        return Optional.ofNullable(resolve(token).get(MEMBER_ID_CLAIM_KEY, Long.class));
    }

    public Optional<String> resolveSocialToken(Token token) {
        return Optional.ofNullable(resolve(token).get(SOCIAL_TOKEN_CLAIM_KEY, String.class));
    }

}
