package gift.web.jwt;

import gift.service.member.MemberService;
import gift.web.dto.Token;
import gift.web.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class AuthUserArgumentResolver implements HandlerMethodArgumentResolver {
    private MemberService memberService;
    private final JwtUtils jwtUtils;

    public AuthUserArgumentResolver(MemberService memberService, JwtUtils jwtUtils) {
        this.memberService = memberService;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("헤더가 유효하지 않음");
        }

        Token token = new Token(authHeader.substring("Bearer ".length()));

        Claims claims = jwtUtils.validateAndGetClaims(token);

        String email = claims.get("email", String.class);

        return memberService.getMemberByEmail(email);
    }
}
