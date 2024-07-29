package gift.model.token;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;

public class KakaoToken implements Token {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("token_type")
    private String tokenType;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("expires_in")
    private Integer expiresIn;
    private final LocalDateTime createdAt = LocalDateTime.now();

    public KakaoToken() {
    }

    public KakaoToken(String accessToken, String tokenType, String refreshToken,
        Integer expiresIn) {
        this.accessToken = accessToken;
        this.tokenType = tokenType;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;

    }

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public String getRefreshToken() {
        return refreshToken;
    }

    @Override
    public LocalDateTime getExpiredAt() {
        return createdAt.plusSeconds(expiresIn);
    }

    @Override
    public Integer getExpiresIn() {
        return expiresIn;
    }

    @Override
    public boolean isValid() {
        return getExpiredAt().isAfter(LocalDateTime.now());
    }

    @Override
    public void update(Token newToken) {
        this.accessToken = newToken.getAccessToken();
        this.expiresIn = newToken.getExpiresIn();
    }

}
