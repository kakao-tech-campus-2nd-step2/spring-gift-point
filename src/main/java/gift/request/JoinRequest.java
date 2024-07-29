package gift.request;

import jakarta.validation.constraints.NotBlank;

public record JoinRequest (
    @NotBlank(message = "이메일을 입력해주세요.") String email,
    @NotBlank(message = "비밀번호를 입력해주세요.") String password
) {

}
