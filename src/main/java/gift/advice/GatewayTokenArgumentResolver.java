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
public class GatewayTokenArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(@Nonnull MethodParameter parameter) {
        return parameter.hasParameterAnnotation(GatewayToken.class) && parameter.getParameterType().equals(String.class);
    }

    @Override
    public String resolveArgument(
            @Nonnull MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            @Nonnull NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory
    ) {
        try {
            String gatewayToken = (String) webRequest.getAttribute("gatewayToken", NativeWebRequest.SCOPE_REQUEST);

            if (gatewayToken == null) {
                throw new AuthenticationRequiredException();
            }
            return gatewayToken;
        } catch (ClassCastException exception) {
            throw new AuthenticationFailedException();
        }
    }
}
