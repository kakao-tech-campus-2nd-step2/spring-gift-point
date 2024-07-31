package gift.resolver;

import gift.anotation.LoginMember;
import gift.config.JwtConfig;
import gift.repository.MemberRepository;
import gift.util.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;
    private final JwtConfig jwtConfig;

    public LoginMemberArgumentResolver(JwtUtil jwtUtil, MemberRepository memberRepository, JwtConfig jwtConfig) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
        this.jwtConfig = jwtConfig;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String authorizationHeader = webRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or invalid");
        }

        String token = authorizationHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        Long userId = jwtUtil.getUserId(token);
        if (userId == null) {
            throw new IllegalArgumentException("Invalid JWT token");
        }

        return memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
