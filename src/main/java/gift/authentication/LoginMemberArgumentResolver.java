package gift.authentication;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.security.sasl.AuthenticationException;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Value("${jwt.secret}")
    private String secretKey;
    private static final int BEARER = 7;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserDetails.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String header = request.getHeader("Authorization");
        if (header == null) {
            throw new AuthenticationException("로그인이 필요합니다.");
        }
        if (header.startsWith("Bearer ")) {
            String token = header.substring(BEARER);
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey).build()
                    .parseClaimsJws(token)
                    .getBody();
            Long id = claims.get("id", Long.class);
            String email = claims.get("email", String.class);
            return new UserDetails(id, email);
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
    }
}
