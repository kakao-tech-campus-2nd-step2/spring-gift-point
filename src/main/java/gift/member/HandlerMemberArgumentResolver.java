package gift.member;


import gift.token.JwtProvider;
import gift.token.MemberTokenDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class HandlerMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtProvider jwtProvider;

    public HandlerMemberArgumentResolver(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(MemberTokenDTO.class);
    }

    @Override
    public MemberTokenDTO resolveArgument(
        MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory
    ) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        return jwtProvider.getMemberTokenDTOFromToken(request.getHeader("Authorization"));
    }
}
