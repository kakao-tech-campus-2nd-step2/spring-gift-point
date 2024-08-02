package gift.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
    @Email(message = "유효하지 않은 이메일 형식입니다.")
    @NotBlank(message = "이메일을 입력해야 합니다.")
    String email,

    String password) {

}