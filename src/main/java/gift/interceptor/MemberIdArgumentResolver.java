package gift.interceptor;

import gift.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberIdArgumentResolver implements HandlerMethodArgumentResolver {

    private final TokenService tokenService;

    public MemberIdArgumentResolver(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(MemberId.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = extractTokenInHeader(request.getHeader("Authorization"));
        return tokenService.getMemberId(token);
    }

    private String extractTokenInHeader(String authHeader) {
        return authHeader.substring("Bearer ".length()).trim();
    }
}
