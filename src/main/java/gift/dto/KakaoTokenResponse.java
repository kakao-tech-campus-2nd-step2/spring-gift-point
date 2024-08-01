package gift.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class KakaoTokenResponse {

    private String tokenType;
    private String accessToken;
    private int expiresIn;
    private String refreshToken;
    private String scope;
    private int refreshTokenExpiresIn;


    public String getTokenType() {
        return tokenType;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getScope() {
        return scope;
    }

    public int getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }
}
