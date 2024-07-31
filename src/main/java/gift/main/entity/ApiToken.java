package gift.main.entity;

import gift.main.dto.KakaoToken;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ApiToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(nullable = false)
    private String accessToken;

    @Column(nullable = false)
    private String refreshToken;

    private LocalDateTime accessTokenExpirationDate;

    public ApiToken(User user, KakaoToken kakaoToken) {
        this.user = user;
        this.accessToken = kakaoToken.accessToken();
        this.refreshToken = kakaoToken.refreshToken();
        this.accessTokenExpirationDate = kakaoToken.accessTokenExpirationDate();
    }

    public ApiToken() {

    }

    public void updete(KakaoToken renewToken) {
        this.accessToken = renewToken.accessToken();
        if (renewToken.refreshToken() != null && !renewToken.refreshToken().isBlank() && !renewToken.refreshToken().equals("null")) {
            this.refreshToken = renewToken.refreshToken();
        }
        this.accessTokenExpirationDate = renewToken.accessTokenExpirationDate();
    }

    public long getId() {
        return id;
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

    public LocalDateTime getAccessTokenExpirationDate() {
        return accessTokenExpirationDate;
    }
}
