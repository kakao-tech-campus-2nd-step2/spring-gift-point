package gift.dto.OAuth;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public record AuthTokenResponse(

        String accessToken,

        String tokenType,

        String refreshToken,

        int expiresIn,

        String scope,

        int refreshTokenExpiresIn
) {
}