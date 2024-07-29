package gift.jwt;

import gift.member.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final JwtService jwtService;

    public LoginMethodArgumentResolver(JwtService jwtService){
        this.jwtService = jwtService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);

        System.out.println("Parameter Type : " + parameter.getParameterType());
        return hasLoginAnnotation;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        String token = jwtService.extractToken(request);
        UserDTO userDTO = jwtService.getUserDTO(token);
        System.out.println(userDTO);
        return userDTO;
    }
}
