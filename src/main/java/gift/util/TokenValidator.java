package gift.util;

import static gift.util.constants.MemberConstants.INVALID_AUTHORIZATION_HEADER;

import gift.exception.member.InvalidTokenException;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class TokenValidator {

    private final JWTUtil jwtUtil;

    public TokenValidator(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    public void validateToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new InvalidTokenException(INVALID_AUTHORIZATION_HEADER);
        }

        String token = authorizationHeader.substring(7);
        Claims claims = jwtUtil.parseToken(token);
        Long memberId = claims.get("memberId", Long.class);
        request.setAttribute("memberId", memberId);
    }
}
