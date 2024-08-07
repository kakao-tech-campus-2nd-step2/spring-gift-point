package gift.global.argumentresolver;

import gift.global.exception.InvalidAccessTokenException;
import gift.global.util.JwtProvider;
import gift.member.entity.Member;
import gift.member.exception.NoSuchMemberException;
import gift.member.repository.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    @Autowired
    public LoginMemberArgumentResolver(MemberRepository memberRepository, JwtProvider jwtProvider) {
        this.memberRepository = memberRepository;
        this.jwtProvider = jwtProvider;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }

    @Override
    public Member resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            if (request.getRequestURI().startsWith("/api/products")) {
                return null;
            }
            throw new InvalidAccessTokenException();
        }
        String accessToken = authorization.substring(7);
        String email = jwtProvider.parseAccessToken(accessToken);
        return memberRepository.findById(email)
            .orElseThrow(NoSuchMemberException::new);
    }
}
