package gift.api.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record TokenResponse(
    String tokenType,
    String accessToken,
    Integer expiresIn,
    String refreshToken,
    Integer refreshTokenExpiresIn
) {}
