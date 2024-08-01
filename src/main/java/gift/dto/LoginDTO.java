package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginDTO(
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "유효한 이메일을 입력하세요.")
    String email,

    @NotBlank(message = "비밀번호를 입력하세요.")
    String password
) {

}