package gift.controller.user.dto;

import gift.common.enums.SocialType;
import gift.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    public record Create(
        @NotBlank
        String password,
        @Email
        String email,
        @NotBlank
        String name
        ) {

        public User toEntity() {
            return new User(password, email, name, SocialType.DEFAULT);
        }
    }

    public record Login(
        @NotBlank
        String password,
        @Email
        String email) {
    }

    public record Point(
        @Min(0)
        int depositPoint
    ) {
    }
}
