package gift.main.resolver;

import gift.main.annotation.SessionUser;
import gift.main.dto.UserVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SessionUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override //주어진 메서드의 파라미터가 지원할 수 있는 타입인지 검사하고, 아닐경우 false
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserVo.class)
                && parameter.hasParameterAnnotation(SessionUser.class);
    }


    @Override //넣어줄 반환값을 넣어준다.
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        HttpSession session = request.getSession();
        return session.getAttribute("user");
    }

}
