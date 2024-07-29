package gift.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequestDto(
        @NotBlank(message = "이메일을 입력해 주세요")
        @Email(message = "이메일 형식을 맞춰 주세요")
        String email,

        @NotBlank(message = "비밀번호를 입력해 주세요")
        String password
){
}
