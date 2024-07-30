package gift.resolver;

import gift.entity.KakaoUser;
import gift.entity.User;
import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import gift.repository.KakaoUserRepository;
import gift.repository.UserRepository;
import gift.service.TokenService;
import gift.util.AuthorizationHeaderProcessor;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Map;
import java.util.Optional;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final TokenService tokenService;
    private final AuthorizationHeaderProcessor authorizationHeaderProcessor;

    public LoginMemberArgumentResolver(UserRepository userRepository, KakaoUserRepository kakaoUserRepository, TokenService tokenService, AuthorizationHeaderProcessor authorizationHeaderProcessor) {
        this.userRepository = userRepository;
        this.kakaoUserRepository = kakaoUserRepository;
        this.tokenService = tokenService;
        this.authorizationHeaderProcessor = authorizationHeaderProcessor;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(gift.annotation.LoginMember.class) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, org.springframework.web.bind.support.WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = authorizationHeaderProcessor.extractToken(request);

        if (token == null || token.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        Map<String, String> userInfo = tokenService.extractUserInfo(token);
        String userType = userInfo.get("userType");
        String id = userInfo.get("id");

        Optional<User> user = findUserByIdAndType(id, userType);

        if (user.isEmpty()) {
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }

        return user.get();
    }

    private Optional<User> findUserByIdAndType(String id, String userType) {
        if ("kakao".equals(userType)) {
            try {
                Long kakaoId = Long.parseLong(id);
                Optional<KakaoUser> kakaoUser = kakaoUserRepository.findById(kakaoId);
                return kakaoUser.map(KakaoUser::getUser);
            } catch (NumberFormatException e) {
                throw new BusinessException(ErrorCode.INVALID_TOKEN, "Invalid Kakao ID format");
            }
        } else {
            return userRepository.findByEmail(id);
        }
    }
}
