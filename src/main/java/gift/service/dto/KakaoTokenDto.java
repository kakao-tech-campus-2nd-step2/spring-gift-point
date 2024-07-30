package gift.service.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record KakaoTokenDto(
        String accessToken,
        String tokenType,
        String refreshToken,
        Long expiresIn,
        String scope,
        Long refreshTokenExpiresIn
) {
}
