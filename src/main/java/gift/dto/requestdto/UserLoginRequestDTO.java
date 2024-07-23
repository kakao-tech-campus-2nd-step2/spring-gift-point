package gift.dto.requestdto;

import gift.domain.User;
import jakarta.validation.constraints.Email;

public record UserLoginRequestDTO(
    @Email(message = "이메일 형식이 아닙니다.")
    String email,
    String password) {

    public User toEntity() {
        return new User(email, password, null);
    }
}
