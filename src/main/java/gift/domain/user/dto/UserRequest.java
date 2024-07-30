package gift.domain.user.dto;

import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.Role;
import gift.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "회원 등록 정보")
public record UserRequest(
    @Schema(description = "회원 이름")
    String name,

    @NotBlank(message = "이메일은 필수 입력 필드입니다.")
    @Email(message = "잘못된 이메일 형식입니다.")
    @Schema(description = "회원 이메일")
    String email,

    @Size(min = 4, max = 20, message = "4-20자 사이의 비밀번호를 입력해주세요.")
    @Pattern(regexp = "[a-zA-z0-9!@^&\\-_]+", message = "영어 대소문자와 숫자, !,@,^,&,-,_ 만 사용 가능합니다.")
    @Schema(description = "회원 비밀번호")
    String password
) {
    public User toUser() {
        return new User(null, name, email, password, Role.USER, AuthProvider.LOCAL);
    }
}
