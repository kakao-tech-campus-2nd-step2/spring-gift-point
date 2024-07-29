package gift.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;

@JsonNaming(value = PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Response DTO for Kakao Token")
public record KakaoTokenResponse(
    @Schema(description = "Access token from Kakao", example = "access_token_example")
    String accessToken,
    @Schema(description = "Token type", example = "bearer")
    String tokenType,
    @Schema(description = "Refresh token from Kakao", example = "refresh_token_example")
    String refreshToken,
    @Schema(description = "Expires in seconds", example = "3600")
    Integer expiresIn,
    @Schema(description = "Scope of the token", example = "profile")
    String scope) {

}
