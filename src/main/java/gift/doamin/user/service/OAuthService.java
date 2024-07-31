package gift.doamin.user.service;

import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;

import gift.doamin.user.dto.KakaoOAuthTokenResponse;
import gift.doamin.user.dto.KakaoOAuthUserInfoResponse;
import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.entity.RefreshToken;
import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import gift.doamin.user.properties.KakaoClientProperties;
import gift.doamin.user.properties.KakaoProviderProperties;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.user.repository.KakaoOAuthTokenRepository;
import gift.doamin.user.repository.RefreshTokenRepository;
import gift.doamin.user.util.AuthorizationOAuthUriBuilder;
import gift.global.JwtProvider;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

@Service
public class OAuthService {

    private final KakaoClientProperties clientProperties;
    private final KakaoProviderProperties providerProperties;
    private final JpaUserRepository userRepository;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final KakaoOAuthTokenRepository kakaoOAuthTokenRepository;
    private final RestClient restClient = RestClient.builder().build();

    public OAuthService(KakaoClientProperties clientProperties,
        KakaoProviderProperties providerProperties, JpaUserRepository userRepository,
        JwtProvider jwtProvider, RefreshTokenRepository refreshTokenRepository,
        KakaoOAuthTokenRepository kakaoOAuthTokenRepository) {
        this.clientProperties = clientProperties;
        this.providerProperties = providerProperties;
        this.userRepository = userRepository;
        this.jwtProvider = jwtProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.kakaoOAuthTokenRepository = kakaoOAuthTokenRepository;
    }

    public String getAuthUrl() {
        return new AuthorizationOAuthUriBuilder()
            .clientProperties(clientProperties)
            .providerProperties(providerProperties)
            .build();
    }

    public KakaoOAuthTokenResponse requestToken(String authorizeCode) {

        String tokenUri = providerProperties.tokenUri();

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("code", authorizeCode);
        body.add("redirect_uri", clientProperties.redirectUri());
        body.add("client_id", clientProperties.clientId());
        body.add("client_secret", clientProperties.clientSecret());

        ResponseEntity<KakaoOAuthTokenResponse> entity = restClient.post()
            .uri(tokenUri)
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoOAuthTokenResponse.class);

        return entity.getBody();
    }

    @Transactional
    public String authenticate(KakaoOAuthTokenResponse tokenResponse) {

        ResponseEntity<KakaoOAuthUserInfoResponse> entity = restClient.get()
            .uri(providerProperties.userInfoUri())
            .header("Authorization", "Bearer " + tokenResponse.getAccessToken())
            .header("Content-Type", "application/x-www-form-urlencoded")
            .retrieve()
            .toEntity(KakaoOAuthUserInfoResponse.class);

        Long id = entity.getBody().getId();
        String nickname = entity.getBody().getProperties().get("nickname");
        String email = id + "@kakao.oauth";

        User user = userRepository.findByEmail(email)
            .orElseGet(
                () -> userRepository.save(new User(email, id.toString(), nickname, UserRole.USER))
            );

        saveToken(user, tokenResponse);

        String myRefreshToken = jwtProvider.generateRefreshToken();
        RefreshToken refreshToken = refreshTokenRepository.findByUser(user)
            .orElseGet(() -> new RefreshToken(myRefreshToken, user));
        refreshToken.setToken(myRefreshToken);
        refreshTokenRepository.save(refreshToken);

        return myRefreshToken;
    }

    @Transactional
    public void saveToken(User user, KakaoOAuthTokenResponse tokenData) {
        LocalDateTime now = LocalDateTime.now().minusMinutes(10);
        LocalDateTime access_token_expires_at = now.plusSeconds(
            Long.parseLong(tokenData.getExpiresIn()));

        LocalDateTime refresh_token_expires_at = null;
        if (tokenData.getRefreshTokenExpiresIn() != null) {
            refresh_token_expires_at = now.plusSeconds(
                Long.parseLong(tokenData.getRefreshTokenExpiresIn()));
        }

        KakaoOAuthToken kakaoOAuthToken = kakaoOAuthTokenRepository.findByUser(user)
            .orElseGet(() -> new KakaoOAuthToken(user));

        kakaoOAuthToken.update(tokenData.getAccessToken(), access_token_expires_at,
            tokenData.getRefreshToken(), refresh_token_expires_at);

        kakaoOAuthTokenRepository.save(kakaoOAuthToken);
    }

    @Transactional
    public void renewOAuthTokens(KakaoOAuthToken kakaoOAuthToken) {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "refresh_token");
        body.add("client_id", clientProperties.clientId());
        body.add("client_secret", clientProperties.clientSecret());
        body.add("refresh_token", kakaoOAuthToken.getRefreshToken());

        ResponseEntity<KakaoOAuthTokenResponse> entity = restClient.post()
            .uri("https://kauth.kakao.com/oauth/token")
            .contentType(APPLICATION_FORM_URLENCODED)
            .body(body)
            .retrieve()
            .toEntity(KakaoOAuthTokenResponse.class);

        saveToken(kakaoOAuthToken.getUser(), entity.getBody());
    }
}
