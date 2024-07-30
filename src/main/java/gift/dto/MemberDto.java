package gift.dto;

import gift.constants.ErrorMessage;
import gift.constants.RegularExpression;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class MemberDto {

    private Long id;

    @NotBlank(message = ErrorMessage.MEMBER_EMAIL_NOT_BLANK_MSG)
    @Email(message = ErrorMessage.MEMBER_NOT_EMAIL_FORMAT_MSG)
    private String email;

    @NotBlank(message = ErrorMessage.MEMBER_PASSWORD_NOT_BLANK_MSG)
    @Size(min = 1, max = 15, message = ErrorMessage.MEMBER_PASSWORD_INVALID_LENGTH_MSG)
    @Pattern(regexp = RegularExpression.MEMBER_PASSWORD_VALID_REGEX, message = ErrorMessage.MEMBER_PASSWORD_INVALID_PATTERN_MSG)
    private String password;

    protected MemberDto() {
    }

    public MemberDto(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
