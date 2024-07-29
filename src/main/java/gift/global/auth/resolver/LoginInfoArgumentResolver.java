package gift.global.auth.resolver;

import gift.global.auth.Authenticate;
import gift.global.auth.LoginInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class LoginInfoArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(Authenticate.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest,
        org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        Long memberId = Long.parseLong((String) request.getAttribute("memberId"));
        String name = (String) request.getAttribute("name");
        return new LoginInfo(memberId, name);
    }
}
