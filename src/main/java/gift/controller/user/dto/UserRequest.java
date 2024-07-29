package gift.controller.user.dto;

import gift.common.enums.SocialType;
import gift.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class UserRequest {

    public record Create(
        @NotBlank
        String password,
        @Email
        String email) {

        public User toEntity() {
            return new User(password, email, SocialType.DEFAULT);
        }
    }

    public record Update(
        @NotBlank
        String password,
        @Email
        String email) {
    }
}
