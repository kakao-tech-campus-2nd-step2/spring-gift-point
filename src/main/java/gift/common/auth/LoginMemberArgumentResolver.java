package gift.common.auth;

import gift.member.MemberService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;
    private final AuthService authService;

    public LoginMemberArgumentResolver(MemberService memberService, AuthService authService) {
        this.memberService = memberService;
        this.authService = authService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginMember.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Long id = authService.getMemberId(webRequest.getHeader("Authorization"));
        return memberService.selectLoginMemberById(id);
    }
}
