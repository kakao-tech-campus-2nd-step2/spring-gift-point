package gift.token;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Token {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(nullable = false)
    private Long userId;
    @Column(nullable = false)
    private String sns;
    @Column(nullable = false)
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private int refreshTokenExpiresIn;

    public Token() {
    }

    public Token(Long userId, String sns, String accessToken, int expiresIn, String refreshToken,
        int refreshTokenExpiresIn) {
        this.userId = userId;
        this.sns = sns;
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiresIn = refreshTokenExpiresIn;
    }

    public String getAccessToken(){
        return accessToken;
    }
}
