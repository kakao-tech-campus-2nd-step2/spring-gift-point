package gift.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class Token extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull

    private String accessToken;
    @NotNull
    private String refreshToken;
    @NotNull
    private Long expiresIn;

    @OneToOne
    @NotNull
    private User user;

    protected Token() {
    }


    public Token(String accessToken, String refreshToken, User user, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
        this.expiresIn = expiresIn;
    }

    public Long getId() {
        return id;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public User getUser() {
        return user;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public boolean isTokenExpired() {
        LocalDateTime expirationTime = this.getLastModifiedAt().plusSeconds(expiresIn);
        return LocalDateTime.now().isAfter(expirationTime);
    }

    public void updateToken(String accessToken, String refreshToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
    }
}
