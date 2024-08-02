package gift.api.member.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import gift.api.member.domain.Member;
import gift.api.member.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record MemberRequest(
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Must be in email format")
    String email,
    @NotBlank(message = "Password is mandatory")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)\\S{8,}$", message = "Must contain upper and lower case letters, numbers, and no blanks")
    String password,
    @NotNull(message = "Role is mandatory")
    Role role
) {
    public Member toEntity() {
        return new Member(email, password, role);
    }
}
