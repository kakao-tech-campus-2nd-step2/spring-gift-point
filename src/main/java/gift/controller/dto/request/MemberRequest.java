package gift.controller.dto.request;

import gift.common.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record MemberRequest(
        @NotBlank(message = "이메일을 입력해주세요.")
        @Email
        String email,
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password,
        @Pattern(regexp = "ADMIN|USER", message = "Must be a valid role: ADMIN, USER")
        Role role) {
}
