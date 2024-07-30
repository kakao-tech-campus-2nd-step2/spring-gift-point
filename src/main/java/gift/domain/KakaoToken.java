package gift.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "kakao_token")
public class KakaoToken {

    @Id
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    protected KakaoToken() {

    }

    public KakaoToken(String email, String accessToken, String refreshToken) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
