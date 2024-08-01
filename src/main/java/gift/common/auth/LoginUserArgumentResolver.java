package gift.common.auth;

import gift.common.annotation.LoginUser;
import gift.common.exception.ErrorCode;
import gift.common.exception.UserException;
import gift.model.User;
import gift.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public LoginUserArgumentResolver(JwtTokenProvider jwtTokenProvider, UserRepository userRepository) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String token = request.getHeader("Authorization").substring(7);
        jwtTokenProvider.validateToken(token);
        String email = jwtTokenProvider.extractEmail(token);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
        return new LoginInfo(user.getId(), user.getPassword(), user.getEmail());
    }
}
