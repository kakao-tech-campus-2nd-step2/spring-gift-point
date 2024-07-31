package gift.user.dto.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record UserLoginRequest(
    @Email
    @Schema(description = "사용자 이메일")
    String email,

    @NotEmpty
    @Schema(description = "사용자 비밀번호")
    String password,

    @Schema(description = "카카오 API 토큰", hidden = true)
    String accessToken
) {

}
