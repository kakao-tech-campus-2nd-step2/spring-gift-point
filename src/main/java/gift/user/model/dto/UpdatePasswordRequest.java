package gift.user.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdatePasswordRequest(
        @NotBlank(message = "기존 비밀번호는 필수 항목입니다.")
        String oldPassword,

        @NotBlank(message = "새로운 비밀번호는 필수 항목입니다.")
        @Size(min = 4, message = "비밀번호는 4자리 이상 입력해주세요.")
        String newPassword
) {
}
