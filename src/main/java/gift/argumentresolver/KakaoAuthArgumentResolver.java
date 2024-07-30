package gift.argumentresolver;

import gift.exception.UnauthorizedException;
import gift.model.KakaoAuthInfo;
import gift.service.KakaoApiService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class KakaoAuthArgumentResolver implements HandlerMethodArgumentResolver {
    private final KakaoApiService kakaoApiService;
    public KakaoAuthArgumentResolver(KakaoApiService kakaoApiService) {
        this.kakaoApiService = kakaoApiService;
    }
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return KakaoAuthInfo.class.isAssignableFrom(parameter.getParameterType());
    }
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
        NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Authorization 헤더 값이 없거나 유효하지 않습니다.");
        }
        String token = authHeader.substring(7);

        return new KakaoAuthInfo(token);
    }

}
