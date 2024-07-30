package gift.user.application;

import gift.user.domain.KakaoLoginResponseDTO;
import gift.user.domain.KakaoProfile;
import gift.user.domain.User;
import gift.user.domain.UserRegisterRequest;
import gift.util.JwtUtil;
import gift.util.TokenResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.stream.Collectors;

@Service
public class KakaoOauthService {

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @Value("${kakao.oauth.token.uri}")
    private String tokenUri;

    @Value("${kakao.oauth.access.uri}")
    private String accessUri;

    @Value("${kakao.oauth.profile.uri}")
    private String profileUri;

    @Value("${kakao.oauth.logout.uri}")
    private String logoutUri;

    private final RestClient restClient;
    private final UserService userManageService;
    private final JwtUtil jwtUtil;

    public KakaoOauthService(RestClient restClient, UserService userManageService, JwtUtil jwtUtil) {
        this.restClient = restClient;
        this.userManageService = userManageService;
        this.jwtUtil = jwtUtil;
    }

    public String getKakaoLoginUrl() {
        Map<String, String> queryparams = Map.of(
                "client_id", clientId,
                "redirect_uri", redirectUri,
                "response_type", "code"
        );

        String queryString = queryparams.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        return tokenUri + queryString;
    }

    public KakaoLoginResponseDTO getKakaoAccessToken(String code) {
        Map<String, String> queryParams = Map.of(
                "grant_type", "authorization_code",
                "client_id", clientId,
                "redirect_uri", redirectUri,
                "code", code
        );

        String queryString = queryParams.entrySet()
                .stream()
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));

        String uri = accessUri + queryString;

        return restClient
                .post()
                .uri(uri)
                .header("Content-Type", "application/x-www-form-urlencoded;charset=utf-8")
                .retrieve()
                .body(KakaoLoginResponseDTO.class);
    }

    public KakaoProfile getKakaoUserProfile(String accessToken) {
        String uri = profileUri;

        return restClient
                .get()
                .uri(uri)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .body(KakaoProfile.class);
    }

    public void getKakaoLogout(String accessToken) {
        String uri = logoutUri;
        restClient
                .post()
                .uri(uri)
                .header("Authorization", "Authorization: KakaoAK " + accessToken)
                .retrieve()
                .body(KakaoProfile.class);
    }

    public TokenResponse processKakaoLoginAndGenerateAccessToken(String code) {
        // 1. 카카오에서 액세스 토큰 획득
        KakaoLoginResponseDTO tokenResponse = getKakaoAccessToken(code);
        String accessToken = tokenResponse.getAccessToken();

        // 2. 액세스 토큰을 사용하여 사용자 프로필 가져오기
        validateToken(accessToken);
        KakaoProfile profile = getKakaoUserProfile(accessToken);

        // 3. 사용자 정보 저장
        UserRegisterRequest registerRequest = new UserRegisterRequest(profile.getNickname(), profile.getProfileImage(), profile.getId());
        User user = userManageService.registerUser(registerRequest);

        // 4. JWT 액세스 토큰 및 리프레시 토큰 생성
        TokenResponse token = jwtUtil.generateTokenResponse(user);

        return token;
    }


    public void validateToken(String accessToken) {
    }
}


