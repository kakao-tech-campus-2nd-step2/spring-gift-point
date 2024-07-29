package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response DTO for User Token")
public class UserResponse {

    @Schema(description = "User token", example = "token_example")
    private String token;

    public UserResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
