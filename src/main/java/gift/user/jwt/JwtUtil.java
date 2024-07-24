package gift.user.jwt;

import io.jsonwebtoken.Jwts;
import javax.crypto.SecretKey;

public final class JwtUtil {
    private JwtUtil() {
    }

    public static final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();
    public static final long expirationSeconds = 3600; // 1시간
    public static final String ISSUER = "KaKaoGiftServer";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final int TOKEN_BEGIN_INDEX = 7;
}
