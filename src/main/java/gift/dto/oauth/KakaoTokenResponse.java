package gift.dto.oauth;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "카카오 토큰 응답 데이터")
public record KakaoTokenResponse(
    @Schema(description = "액세스 토큰", example = "abc123")
    String accessToken,

    @Schema(description = "액세스 토큰 만료 시간(초)", example = "3600")
    Integer expiresIn,

    @Schema(description = "리프레시 토큰", example = "def456")
    String refreshToken,

    @Schema(description = "리프레시 토큰 만료 시간(초)", example = "86400")
    Integer refreshTokenExpiresIn
) {

}
