package gift.dto.requestdto;

import gift.domain.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record UserSignupRequestDTO(
    @Email(message = "이메일 형식이 아닙니다.")
    String email,
    String password,
    @Pattern(regexp = "^(관리자|일반)$", message = "권한이 잘못 설정되었습니다.")
    String role) {

    public User toEntity() {
        return new User(email, password, role);
    }
}
