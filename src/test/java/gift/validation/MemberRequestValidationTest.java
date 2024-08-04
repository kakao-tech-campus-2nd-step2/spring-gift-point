package gift.validation;

import gift.member.dto.MemberRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

public class MemberRequestValidationTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

    @BeforeEach
    public void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterEach
    public void close() {
        validatorFactory.close();
    }

    @Test
    @DisplayName("정상 회원 유효성 검사 테스트")
    void checkNormalMember() {
        MemberRequest request = new MemberRequest(
                "test@email.com",
                "password"
        );

        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이메일이 없는 회원 유효성 검사 테스트")
    void checkEmptyEmailMember() {
        MemberRequest request = new MemberRequest(
                "",
                "password"
        );

        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("유효하지 않은 이메일인 회원 유효성 검사 테스트")
    void checkInvalidEmailMember() {
        MemberRequest request = new MemberRequest(
                "test_email.com",
                "password"
        );

        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("비밀번호가 없는 회원 유효성 검사 테스트")
    void checkEmptyPasswordMember() {
        MemberRequest request = new MemberRequest(
                "test@email.com",
                ""
        );

        Set<ConstraintViolation<MemberRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

}
