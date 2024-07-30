package gift.resolver;

import gift.annotation.LoginUser;
import gift.entity.Member;
import gift.exception.CustomException;
import gift.exception.ErrorCode;
import gift.service.MemberService;
import gift.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private final JwtUtil jwtUtil;
    private final MemberService memberService;

    public LoginUserHandlerMethodArgumentResolver(JwtUtil jwtUtil, MemberService memberService) {
        this.jwtUtil = jwtUtil;
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
        throws CustomException {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization");

        if (token == null) {
            throw new CustomException(ErrorCode.TOKEN_NOT_EXISTS);
        }

        if (!jwtUtil.isTokenExpired(token)) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        String email = jwtUtil.getEmailFromToken(token);
        if (memberService.findByEmail(email).getId() == null) {
            throw new CustomException(ErrorCode.TOKEN_INVALID);
        }

        return email;

    }

}
