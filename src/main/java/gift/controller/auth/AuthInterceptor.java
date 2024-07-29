package gift.controller.auth;

import gift.config.properties.JwtProperties;
import gift.exception.UnauthorizedAccessException;
import gift.model.MemberRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.crypto.SecretKey;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtProperties jwtProperties;

    public AuthInterceptor(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        var header = getHeader(request);
        var token = getTokenWithAuthorizationHeader(header);
        var claims = getClaimsWithToken(token);
        setMemberInformationWithClaims(request, claims);
        return true;
    }

    private Claims getClaimsWithToken(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private void setMemberInformationWithClaims(HttpServletRequest request, Claims claims) {
        var memberId = Long.parseLong(claims.getSubject());
        var memberRole = MemberRole.valueOf(claims.get("role").toString());
        request.setAttribute("memberId", memberId);
        request.setAttribute("memberRole", memberRole);
    }

    private String getTokenWithAuthorizationHeader(String authorizationHeader) {
        var header = authorizationHeader.split(" ");
        if (header.length != 2) throw new IllegalArgumentException("잘못된 헤더 정보입니다.");
        return header[1];
    }

    private String getHeader(HttpServletRequest request) {
        var header = request.getHeader("Authorization");
        if (header == null) throw new UnauthorizedAccessException("인가되지 않은 요청입니다.");
        return header;
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.secretKey().getBytes());
    }
}
