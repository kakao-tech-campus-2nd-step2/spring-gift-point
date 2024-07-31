package gift.util.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "JWT 응답 Dto")
public record JwtResponse(
    @Schema(description = "JWT 토큰", example = "header.payload.signature")
    String token) {

}
