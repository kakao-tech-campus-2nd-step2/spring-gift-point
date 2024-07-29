package gift.domain;

import gift.annotation.LoginUser;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {


    private final JwtToken jwtToken;

    public LoginUserArgumentResolver(JwtToken jwtToken) {
        this.jwtToken = jwtToken;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
            String token = request.getHeader("Authorization");

            if(token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                return jwtToken.tokenToEmail(token);
            }
            throw new JwtException("권한이 없습니다.");
    }

}
