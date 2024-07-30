package gift.token;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "토큰 응답 DTO")
public class TokenResponseDTO {

    @Schema(description = "액세스 토큰", example = "header.payload.signature")
    String accessToken;

    public TokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }
}
