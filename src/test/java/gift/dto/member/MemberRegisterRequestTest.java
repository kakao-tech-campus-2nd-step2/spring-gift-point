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

public class MemberRegisterRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 회원 가입 요청")
    public void testRegisterMemberValid() {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(
            "valid@example.com",
            "validpassword"
        );

        Set<ConstraintViolation<MemberRegisterRequest>> violations = validator.validate(
            memberRegisterRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일이 null인 회원 가입 요청")
    public void testRegisterMemberNullEmail() {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(
            null,
            "validpassword"
        );

        Set<ConstraintViolation<MemberRegisterRequest>> violations = validator.validate(
            memberRegisterRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("email") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("비밀번호가 null인 회원 가입 요청")
    public void testRegisterMemberNullPassword() {
        MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest(
            "valid@example.com",
            null
        );

        Set<ConstraintViolation<MemberRegisterRequest>> violations = validator.validate(
            memberRegisterRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("password") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }
}
