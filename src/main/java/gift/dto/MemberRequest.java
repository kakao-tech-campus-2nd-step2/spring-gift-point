package gift.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record MemberRequest(
        @Email(message = "이메일 형식의 입력이어야 합니다.")
        @NotBlank
        String email,
        @NotBlank
        String password
) {
}
