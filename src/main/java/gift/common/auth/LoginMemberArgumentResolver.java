package gift.common.auth;

import gift.common.exception.UserNotFoundException;
import gift.member.model.Member;
import gift.member.repository.MemberRepository;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;
    private final MemberRepository memberRepository;

    public LoginMemberArgumentResolver(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean supports = parameter.hasParameterAnnotation(LoginMember.class) && parameter.getParameterType().equals(Member.class);
        System.out.println("supportsParameter: " + supports);
        return supports;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        System.out.println("resolveArgument called");

        // header 검증
        String header = webRequest.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            throw new UserNotFoundException("Missing or invalid Authorization header");
        }

        // JWT 토큰 검증
        String token = header.substring(7); // "Bearer " 접두사 제거
        if (!jwtUtil.isTokenValid(token)) {
            throw new UserNotFoundException("Invalid JWT token");
        }

        // 토큰에서 이메일 추출
        String email = jwtUtil.extractEmail(token);
        Optional<Member> member = memberRepository.findByEmail(email);
        System.out.println("member = " + member.orElse(null));
        System.out.println("member.get().getId() = " + member.map(Member::getId).orElse(null));

        if (member.isEmpty()) {
            throw new UserNotFoundException("User not found with email: " + email);
        }

        return member.get();
    }
}
