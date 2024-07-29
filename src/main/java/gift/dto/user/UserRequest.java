package gift.dto.user;

import gift.common.enums.LoginType;
import gift.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;

public class UserRequest {
    @Schema(description = "유저 등록 request")
    public record Create(
            @Schema(description = "이메일", example = "example@example.com")
            String email,
            @Schema(description = "패스워드", example = "password123")
            String password
    ) {
        public User toEntity() {
            return new User(this.email, this.password, LoginType.DEFAULT);
        }
    }

    @Schema(description = "유저 로그인 request")
    public record Check(
            @Schema(description = "이메일", example = "example@example.com")
            String email,
            @Schema(description = "패스워드", example = "password123")
            String password
    ) {

    }
}
