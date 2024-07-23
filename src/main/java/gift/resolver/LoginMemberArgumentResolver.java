package gift.resolver;

import gift.annotation.LoginMember;
import gift.service.MemberService;
import gift.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private MemberService memberService;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(LoginMember.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String sub = (String) webRequest.getAttribute("sub", NativeWebRequest.SCOPE_REQUEST);
        if (sub == null) {
            throw new IllegalArgumentException("JWT에서 회원 ID 없음");
        }

        Long memberId = Long.parseLong(sub);
        return memberService.findById(memberId);
    }
}
