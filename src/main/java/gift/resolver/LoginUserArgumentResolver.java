package gift.resolver;

import gift.exception.auth.UnauthorizedException;
import gift.jwt.JwtUtil;
import gift.model.LoginMember;
import gift.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final MemberService memberService;
    private static final String BEARER = "Bearer ";

    public LoginUserArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER)) {
            throw new UnauthorizedException("인증되지 않은 사용자입니다.");
        }

        String token = authorizationHeader.substring(BEARER.length());

        if (JwtUtil.isExpired(token)) {
            throw new UnauthorizedException("만료된 토큰입니다.");
        }

        return memberService.getMemberByEmail(JwtUtil.getEmail(token));
    }
}
