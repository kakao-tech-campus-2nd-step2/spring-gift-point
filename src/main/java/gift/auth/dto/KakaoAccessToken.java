package gift.auth.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoAccessToken (
    String tokenType,
    String accessToken,
    String idToken,
    Integer expiresIn,
    String refreshToken,
    Integer refreshTokenExpiresIn,
    String scope
) {
}
