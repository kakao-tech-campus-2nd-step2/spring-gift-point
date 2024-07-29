package gift.config;

import static gift.util.JwtUtil.verifyToken;

import gift.controller.auth.AuthMapper;
import gift.controller.auth.LoginResponse;
import gift.controller.auth.Token;
import gift.exception.HttpHeaderNotValidException;
import gift.exception.UnauthorizedException;
import gift.service.MemberService;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginAdminArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginAdminArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginAdmin.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authHeader = webRequest.getHeader("Authorization");
        if (authHeader == null) {
            throw new HttpHeaderNotValidException("Authorization Header is empty");
        }
        if (!authHeader.startsWith("Bearer ")) {
            throw new HttpHeaderNotValidException(
                "Authorization Header does not start with 'Bearer '");
        }
        Token token = new Token(authHeader.substring(7));
        Claims claims;
        try {
            claims = verifyToken(token);
        } catch (Exception e) {
            throw new HttpHeaderNotValidException();
        }
        String email = claims.getSubject();
        LoginResponse loginResponse = AuthMapper.toLoginResponse(memberService.findByEmail(email));
        if (!loginResponse.isAdmin()) {
            throw new UnauthorizedException("is not Admin");
        }
        return loginResponse;
    }
}