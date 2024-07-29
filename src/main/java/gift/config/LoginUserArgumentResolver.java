package gift.config;

import static gift.util.JwtUtil.verifyToken;

import gift.controller.auth.AuthMapper;
import gift.controller.auth.Token;
import gift.exception.HttpHeaderNotValidException;
import gift.service.MemberService;
import io.jsonwebtoken.Claims;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginUserArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
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
        return AuthMapper.toLoginResponse(memberService.findByEmail(email));
    }
}