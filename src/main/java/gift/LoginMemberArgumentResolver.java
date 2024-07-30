package gift;

import gift.classes.Exceptions.AuthException;
import gift.dto.MemberDto;
import gift.services.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final MemberService memberService;

    public LoginMemberArgumentResolver(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginUserAnnotation = parameter.hasParameterAnnotation(LoginMember.class);
        boolean isUserType = MemberDto.class.isAssignableFrom(parameter.getParameterType());
        return hasLoginUserAnnotation && isUserType;
    }

    @Override
    public MemberDto resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String auth = request.getHeader("authorization");
        if (auth == null) {
            throw new AuthException("Authorization header is missing");
        }

        String[] splitted = auth.split(" ");
        if (splitted.length < 2) {
            throw new AuthException("Invalid token format");
        }

        if (!splitted[0].equals("Bearer")) {
            throw new AuthException("Invalid token type");
        }
        String token = splitted[1];

        return memberService.getLoginUser(token);


    }
}
