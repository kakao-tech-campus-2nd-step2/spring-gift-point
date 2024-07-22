package gift.dto.member;

import static gift.util.constants.GeneralConstants.REQUIRED_FIELD_MISSING;
import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MemberLoginRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 회원 로그인 요청")
    public void testLoginMemberValid() {
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(
            "valid@example.com",
            "validpassword"
        );

        Set<ConstraintViolation<MemberLoginRequest>> violations = validator.validate(
            memberLoginRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일이 null인 회원 로그인 요청")
    public void testLoginMemberNullEmail() {
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest(null, "validpassword");

        Set<ConstraintViolation<MemberLoginRequest>> violations = validator.validate(
            memberLoginRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("비밀번호가 null인 회원 로그인 요청")
    public void testLoginMemberNullPassword() {
        MemberLoginRequest memberLoginRequest = new MemberLoginRequest("valid@example.com", null);

        Set<ConstraintViolation<MemberLoginRequest>> violations = validator.validate(
            memberLoginRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }
}
