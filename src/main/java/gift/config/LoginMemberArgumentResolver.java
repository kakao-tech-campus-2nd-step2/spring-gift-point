package gift.config;

import gift.dto.MemberRequestDTO;
import gift.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.stereotype.Component;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger logger = LoggerFactory.getLogger(LoginMemberArgumentResolver.class);
    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginMember.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            logger.error("Authorization 헤더가 존재하지 않거나 유효하지 않습니다.");
            throw new IllegalArgumentException("Authorization 헤더가 존재하지 않거나 유효하지 않습니다.");
        }
        token = token.substring(7);
        logger.debug("추출된 토큰: {}", token);
        String email = memberService.extractEmailFromToken(token); // 토큰에서 이메일 추출

        if (email == null) {
            logger.error("유효하지 않은 토큰입니다.");
            throw new IllegalArgumentException("유효하지 않은 토큰입니다.");
        }

        MemberRequestDTO memberRequestDTO = new MemberRequestDTO();
        memberRequestDTO.setToken(token);
        memberRequestDTO.setEmail(email); // 이메일 설정
        return memberRequestDTO;
    }
}
