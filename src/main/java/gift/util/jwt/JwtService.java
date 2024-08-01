package gift.util.jwt;

import static gift.util.jwt.JwtUtil.ISSUER;
import static gift.util.jwt.JwtUtil.SECRET_KEY;
import static gift.util.jwt.JwtUtil.TOKEN_BEGIN_INDEX;
import static gift.util.jwt.JwtUtil.TOKEN_PREFIX;
import static gift.util.jwt.JwtUtil.expirationSeconds;

import gift.domain.AppUser;
import gift.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    private final UserService userService;

    public JwtService(UserService userService) {
        this.userService = userService;
    }

    public String createToken(Long id) {
        return TOKEN_PREFIX + Jwts.builder()
                .subject(id.toString())
                .issuer(ISSUER)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationSeconds * 1000))
                .signWith(SECRET_KEY)
                .compact();
    }

    public AppUser getLoginUser(String token) {
        Long id = getIdFromToken(token);
        return userService.findUser(id);
    }

    private Long getIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(removeBearerPrefix(token))
                .getPayload();
        return Long.parseLong(claims.getSubject());
    }

    private String removeBearerPrefix(String token) {
        if (!token.startsWith(TOKEN_PREFIX)) {
            throw new SecurityException();
        }
        return token.substring(TOKEN_BEGIN_INDEX);
    }
}
