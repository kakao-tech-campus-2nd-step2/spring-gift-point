package gift.resolver;

import gift.model.Member;
import gift.service.MemberService;
import gift.util.JwtUtil;
import gift.annotation.LoginMember;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_NAME = "Bearer ";

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    public LoginMemberArgumentResolver(MemberService memberService, JwtUtil jwtUtil) {
        this.memberService = memberService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginMember.class) != null
            && parameter.getParameterType().equals(Member.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader(HEADER_NAME);

        if (isTokenPresent(authorizationHeader)) {
            String token = extractToken(authorizationHeader);
            if (jwtUtil.validateToken(token)) {
                return getMemberFromToken(token);
            }
        }
        return null;
    }

    private boolean isTokenPresent(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith(AUTHORIZATION_NAME);
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private Member getMemberFromToken(String token) {
        String email = jwtUtil.getSubject(token);
        return memberService.findMemberByEmail(email);
    }
}