package gift.advice;

import gift.core.domain.authentication.exception.AuthenticationFailedException;
import gift.core.domain.authentication.exception.AuthenticationRequiredException;
import jakarta.annotation.Nonnull;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class LoggedInUserArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(@Nonnull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoggedInUser.class) && parameter.getParameterType().equals(Long.class);
    }

    @Override
    public Long resolveArgument(
            @Nonnull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @Nonnull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        try {
            Long userId = (Long) webRequest.getAttribute("userId", NativeWebRequest.SCOPE_REQUEST);
            if (userId == null) {
                throw new AuthenticationRequiredException();
            }
            return userId;
        } catch (ClassCastException exception) {
            throw new AuthenticationFailedException();
        }
    }
}
