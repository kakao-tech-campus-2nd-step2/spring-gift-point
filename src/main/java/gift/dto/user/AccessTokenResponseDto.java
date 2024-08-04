package gift.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;

public class AccessTokenResponseDto {
    @Schema(description = "access token", nullable = false, example = "9s7fn9awum3p...")
    private String access_token;

    public AccessTokenResponseDto(String access_token) {
        this.access_token = access_token;
    }

    public AccessTokenResponseDto() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
