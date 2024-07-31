package gift.entity;

import io.swagger.v3.oas.annotations.media.Schema;

public class AccessTokenResponse {
    @Schema(description = "access token", nullable = false, example = "9s7fn9awum3p...")
    private String access_token;

    public AccessTokenResponse(String access_token) {
        this.access_token = access_token;
    }

    public AccessTokenResponse() {
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
