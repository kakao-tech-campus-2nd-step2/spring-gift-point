package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public class AccessTokenResponseDTO {
    @Schema(description = "access token", nullable = false, example = "9s7fn9awum3p...")
    private String accessToken;

    public AccessTokenResponseDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    public AccessTokenResponseDTO() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
