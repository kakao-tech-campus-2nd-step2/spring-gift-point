package gift.dto.Response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "액세스 토큰 응답")
public class AccessTokenResponse {

    @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1...")
    private String access_token;

    @Schema(description = "토큰 타입", example = "bearer")
    private String token_type;

    @Schema(description = "리프레시 토큰", example = "eyJhbGciOiJIUzI1...")
    private String refresh_token;

    @Schema(description = "만료 시간(초)", example = "3600")
    private int expires_in;

    @Schema(description = "스코프", example = "profile")
    private String scope;

    public String getAccess_token() {
        return access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public String getScope() {
        return scope;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }
}
