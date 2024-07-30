package gift.common.resolver;

import gift.common.annotation.LoginMember;
import gift.model.user.LoginUserDTO;
import gift.model.user.User;
import gift.service.UserService;
import gift.common.utils.JwtUtil;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginMemberArgumentResolver(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginMember.class);
    }


    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String token = webRequest.getHeader("Authorization").substring(7);
        String email = jwtUtil.extractUsername(token);
        User user = userService.findByEmail(email);
        return new LoginUserDTO(user.getId(), user.getName(), user.getEmail(), user.getRole());

    }
}
