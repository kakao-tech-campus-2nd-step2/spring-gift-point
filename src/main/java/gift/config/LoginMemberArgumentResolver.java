package gift.config;

import gift.dto.MemberDto;
import gift.jwt.JwtTokenProvider;
import gift.model.member.LoginMember;
import gift.model.member.Member;
import gift.service.MemberService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;


@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = LoggerFactory.getLogger(LoginMemberArgumentResolver.class);

    public LoginMemberArgumentResolver(MemberService memberService, JwtTokenProvider jwtTokenProvider) {
        this.memberService = memberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public MemberDto resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        AuthorizationHeader authHeader = new AuthorizationHeader(request.getHeader("Authorization"));
        Member authMember =  getAuthenticatedMember(authHeader);

        return new MemberDto(authMember.getEmail(),authMember.getPassword(), authMember.getRole());
    }

    private Member getAuthenticatedMember(AuthorizationHeader authHeader) {
        String token = authHeader.getToken();
        if (!jwtTokenProvider.validateToken(token)) {
            throw new IllegalStateException("Invalid or missing JWT token");
        }

        String email = jwtTokenProvider.getEmailFromToken(token);
        Optional<Member> member = memberService.findByEmail(email);
      
        return member.orElseThrow(() -> new IllegalStateException("Authenticated member not found in the database."));
    }
}
