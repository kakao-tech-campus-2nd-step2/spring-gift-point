package gift.authentication.restapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email(message = "이메일 형식에 맞게 입력해주세요.")
        @NotEmpty(message = "이메일을 입력해주세요.")
        String email,

        @NotEmpty(message = "비밀번호를 입력해주세요.")
        @Size(min = 6, max = 20, message = "비밀번호는 6자 이상 20자 이하로 입력해주세요.")
        String password
) {
}
