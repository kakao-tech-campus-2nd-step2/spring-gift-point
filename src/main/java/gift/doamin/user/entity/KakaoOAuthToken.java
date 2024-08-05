package gift.doamin.user.entity;

import gift.doamin.user.dto.KakaoOAuthTokenResponse;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;

@Entity
public class KakaoOAuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @OneToOne(optional = false)
    User user;

    @Column(nullable = false)
    String accessToken;

    @Column(nullable = false)
    private LocalDateTime expires_at;

    @Column(nullable = false)
    String refreshToken;

    @Column(nullable = false)
    private LocalDateTime refresh_token_expires_at;

    public KakaoOAuthToken(User user) {
        this.user = user;
    }

    protected KakaoOAuthToken() {
    }

    public User getUser() {
        return user;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void update(KakaoOAuthTokenResponse tokenResponse) {
        // 지연 등을 고려해 만료시간을 10분 당겨서 저장하도록 현재시간-10분 수행
        LocalDateTime now = LocalDateTime.now().minusMinutes(10);
        LocalDateTime access_token_expires_at = now.plusSeconds(
            Long.parseLong(tokenResponse.getExpiresIn()));

        LocalDateTime refresh_token_expires_at = null;
        if (tokenResponse.getRefreshTokenExpiresIn() != null) {
            refresh_token_expires_at = now.plusSeconds(
                Long.parseLong(tokenResponse.getRefreshTokenExpiresIn()));
        }

        this.accessToken = tokenResponse.getAccessToken();
        this.expires_at = access_token_expires_at;

        String refreshToken = tokenResponse.getRefreshToken();
        if (refreshToken != null && refresh_token_expires_at != null) {
            this.refreshToken = refreshToken;
            this.refresh_token_expires_at = refresh_token_expires_at;
        }
    }
}
