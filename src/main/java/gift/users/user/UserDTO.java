package gift.users.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "회원 DTO")
public record UserDTO(Long id,
                      @Schema(description = "회원 이메일", example = "example@kakao.com")
                      @Email
                      @NotBlank(message = "이메일을 입력하지 않았습니다.")
                      String email,
                      @Schema(description = "회원 비밀번호", example = "134ff1")
                      @NotBlank(message = "비밀번호를 입력하지 않았습니다.")
                      String password,
                      String sns,
                      int points) {

    public User toUser() {
        return new User(id, email, password, "local", points);
    }

    public static UserDTO fromUser(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getPassword(), "local", user.getPoints());
    }
}
