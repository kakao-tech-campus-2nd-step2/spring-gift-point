package gift.doamin.user.entity;

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

    public void update(String accessToken, LocalDateTime expires_at,
        String refreshToken, LocalDateTime refresh_token_expires_at) {
        this.accessToken = accessToken;
        this.expires_at = expires_at;
        if (refreshToken != null && refresh_token_expires_at != null) {
            this.refreshToken = refreshToken;
            this.refresh_token_expires_at = refresh_token_expires_at;
        }
    }
}
