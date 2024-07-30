package gift.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;

public record MemberPasswordDTO(
    @NotBlank
    String password,

    @NotBlank
    String newPassword1,

    @NotBlank
    String newPassword2
) {

    @AssertTrue(message = "비밀번호 확인이 일치하지 않습니다.")
    public boolean isNewPasswordMatching() {
        return newPassword1.equals(newPassword2);
    }
}
