package gift.annotation;

import gift.exceptions.CustomException;
import gift.jwtutil.JwtUtil;
import gift.model.User;
import gift.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginUserArgumentResolver(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class) && User.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorizationHeader = webRequest.getHeader("Authorization");

        if (isTokenPresent(authorizationHeader)) {
            String token = extractToken(authorizationHeader);
            if (jwtUtil.validateToken(token)) {
                User user = getUserFromToken(token);
                if (user != null) {
                    return user;
                } else {
                    throw CustomException.userNotFoundException();
                }
            } else {
                throw CustomException.invalidTokenException();
            }
        } else {
            throw CustomException.invalidHeaderException();
        }
    }

    private boolean isTokenPresent(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

    private User getUserFromToken(String token) {
        String email = jwtUtil.getSubject(token);
        return userService.findUser(email);
    }
}