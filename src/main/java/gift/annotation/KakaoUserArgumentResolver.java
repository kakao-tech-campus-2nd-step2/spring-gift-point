package gift.annotation;

import gift.dto.KakaoUserDTO;
import gift.exceptions.CustomException;
import gift.model.User;
import gift.service.KakaoApiService;
import gift.service.UserService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class KakaoUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final KakaoApiService kakaoApiService;
    private final UserService userService;

    public KakaoUserArgumentResolver(KakaoApiService kakaoApiService, UserService userService) {
        this.kakaoApiService = kakaoApiService;
        this.userService = userService;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(KakaoUser.class) && KakaoUserDTO.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        String authorizationHeader = webRequest.getHeader("Authorization");

        if (isTokenPresent(authorizationHeader)) {
            String accessToken = extractToken(authorizationHeader);
            String email = kakaoApiService.getEmailFromToken(accessToken);
            User user = userService.findUser(email);

            if (user != null) {
                return new KakaoUserDTO(user, accessToken);
            }
            else {
                throw CustomException.userNotFoundException();
            }
        }
        else {
            throw CustomException.invalidHeaderException();
        }
    }

    private boolean isTokenPresent(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }

    private String extractToken(String authorizationHeader) {
        return authorizationHeader.substring(7);
    }

}
