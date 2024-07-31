package gift.user.service;

import gift.client.kakao.KakaoApiClient;
import gift.user.dto.request.UserLoginRequest;
import gift.user.dto.request.UserRegisterRequest;
import gift.user.dto.response.UserResponse;
import gift.user.repository.UserJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class KakaoUserService {

    private final KakaoApiClient kakaoApiClient;
    private final UserService userService;
    private final UserJpaRepository userRepository;

    public KakaoUserService(KakaoApiClient kakaoApiClient, UserService userService,
        UserJpaRepository userRepository) {
        this.kakaoApiClient = kakaoApiClient;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    public UserResponse loginKakaoUser(String code) {
        var token = kakaoApiClient.getKakaoTokenResponse(code).accessToken();
        var userInfo = kakaoApiClient.getKakaoUserId(token);
        String email = userInfo.id() + "@kakao.com";
        String password = "";

        if (!userRepository.existsByEmail(email)) {
            userService.registerUser(new UserRegisterRequest(email, password, true, token));
        }

        return userService.loginUser(new UserLoginRequest(email, password, token));
    }

}
