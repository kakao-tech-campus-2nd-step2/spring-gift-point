package gift.domain;

import gift.utils.TimeStamp;
import jakarta.persistence.*;

import java.util.UUID;

import static gift.utils.TokenConstant.EXPIRATION_OFFSET;

@Entity
public class AuthToken extends TimeStamp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private Integer tokenTime;

    @Column(unique = true, nullable = false)
    private String email;

    private String accessToken;

    private Integer accessTokenTime;

    private String refreshToken;

    private Integer refreshTokenTime;

    public AuthToken() {
    }

    public AuthToken(String token, String email) {
        this.token = token;
        this.tokenTime = 30000;
        this.email = email;
    }

    private AuthToken(Builder builder) {
        this.token = builder.token;
        this.tokenTime = builder.tokenTime;
        this.email = builder.email;
        this.accessToken = builder.accessToken;
        this.accessTokenTime = builder.accessTokenTime;
        this.refreshToken = builder.refreshToken;
        this.refreshTokenTime = builder.refreshTokenTime;
    }

    public static class Builder {
        private String token;
        private Integer tokenTime;
        private String email;
        private String accessToken;
        private Integer accessTokenTime;
        private String refreshToken;
        private Integer refreshTokenTime;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder tokenTime(Integer tokenTime) {
            this.tokenTime = tokenTime;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder accessToken(String accessToken) {
            this.accessToken = accessToken;
            return this;
        }

        public Builder accessTokenTime(Integer accessTokenTime) {
            this.accessTokenTime = accessTokenTime;
            return this;
        }

        public Builder refreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
            return this;
        }

        public Builder refreshTokenTime(Integer refreshTokenTime) {
            this.refreshTokenTime = refreshTokenTime;
            return this;
        }

        public AuthToken build() {
            return new AuthToken(this);
        }
    }

    public Long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public Integer getTokenTime() {
        return tokenTime;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Integer getAccessTokenTime() {
        return accessTokenTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Integer getRefreshTokenTime() {
        return refreshTokenTime;
    }

    public void update(TokenInformation tokenInfo){
        this.token = UUID.randomUUID().toString();
        this.tokenTime = tokenInfo.getAccessTokenTime()- EXPIRATION_OFFSET;
        this.accessToken = tokenInfo.getAccessToken();
        this.accessTokenTime = tokenInfo.getAccessTokenTime();

        if(tokenInfo.getRefreshToken() != null){
            this.refreshToken = tokenInfo.getRefreshToken();
            this.refreshTokenTime = tokenInfo.getRefreshTokenTime();
        }
    }
}
