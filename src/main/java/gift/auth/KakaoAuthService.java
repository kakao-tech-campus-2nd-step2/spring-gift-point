package gift.auth;

import gift.client.KakaoApiClient;
import gift.config.KakaoProperties;
import gift.dto.KakaoUserResponse;
import gift.entity.KakaoUser;
import gift.entity.User;
import gift.repository.KakaoUserRepository;
import gift.repository.UserRepository;
import gift.service.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service("kakaoAuthService")
public class KakaoAuthService implements AuthService {

    private final KakaoProperties kakaoProperties;
    private final KakaoApiClient kakaoApiClient;
    private final UserRepository userRepository;
    private final KakaoUserRepository kakaoUserRepository;
    private final TokenService tokenService;

    public KakaoAuthService(KakaoProperties kakaoProperties, KakaoApiClient kakaoApiClient, UserRepository userRepository, KakaoUserRepository kakaoUserRepository, TokenService tokenService) {
        this.kakaoProperties = kakaoProperties;
        this.kakaoApiClient = kakaoApiClient;
        this.userRepository = userRepository;
        this.kakaoUserRepository = kakaoUserRepository;
        this.tokenService = tokenService;
    }

    @Override
    public String getLoginUrl() {
        return kakaoProperties.getAuthUrl() + "?response_type=code&client_id=" + kakaoProperties.getClientId() + "&redirect_uri=" + kakaoProperties.getRedirectUri();
    }

    @Transactional
    public Map<String, String> handleCallback(String authorizationCode) {
        String accessToken = kakaoApiClient.getAccessToken(authorizationCode);
        KakaoUserResponse kakaoUserResponse = kakaoApiClient.getUserInfo(accessToken);

        KakaoUser kakaoUser = kakaoUserRepository.findByKakaoId(kakaoUserResponse.getId())
                .orElseGet(() -> {
                    KakaoUser newKakaoUser = new KakaoUser(kakaoUserResponse.getId(), kakaoUserResponse.getProperties().getNickname(), null);
                    User user = userRepository.save(new User(newKakaoUser));
                    newKakaoUser.setUser(user);
                    return kakaoUserRepository.save(newKakaoUser);
                });

        String jwtToken = tokenService.generateToken(kakaoUser.getUser().getId().toString(), "kakao");

        Map<String, String> tokens = new HashMap<>();
        tokens.put("kakaoAccessToken", accessToken);
        tokens.put("jwtToken", jwtToken);

        return tokens;
    }

    @Override
    public String generateToken(User user) {
        return tokenService.generateToken(user.getId().toString(), "kakao");
    }
}
