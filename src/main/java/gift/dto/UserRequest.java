package gift.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request DTO for user authentication")
public class UserRequest {

    @Schema(description = "Email of the user", example = "testuser@example.com")
    private String email;
    @Schema(description = "Password of the user", example = "password")
    private String password;

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
