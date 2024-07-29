package gift.security;

import gift.DTO.User.UserResponse;
import gift.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class AuthenticateMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${key}")
    private String secretKey;

    public AuthenticateMemberArgumentResolver(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticateMember.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) throws Exception {
        String token = webRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("인증 방식이 잘못되었거나, 토큰이 없습니다!");
        }

        token = token.substring(7); // "Bearer " 부분을 제거

        String userId = jwtTokenProvider.getClaimsFromToken(token);
        if(!userService.isUserIdDuplicate(userId)) {
            throw new IllegalArgumentException("유효하지 않은 로그인 정보입니다!");
        }

        return userService.findByUserId(userId);
    }
}