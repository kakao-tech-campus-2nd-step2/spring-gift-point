package gift.validation;

import gift.product.dto.OptionRequest;
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

public class OptionRequestValidationTest {

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
    @DisplayName("정상 옵션 유효성 검사 테스트")
    void checkNormalOption() {
        OptionRequest request = new OptionRequest("옵션", 10);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("이름이 빈 옵션 유효성 검사 테스트")
    void checkEmptyNameOption() {
        OptionRequest request = new OptionRequest("", 10);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("이름이 50자 초과인 옵션 유효성 검사 테스트")
    void checkLongerNameOption() {
        OptionRequest request = new OptionRequest(
                "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" +
                        "1234567890" + "1", 10);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("이름에 특수 문자가 포함된 옵션 유효성 검사 테스트")
    void checkEscapeCharacterOption() {
        OptionRequest request = new OptionRequest("@ption", 10);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("이름에 사용 가능한 특수 문자가 포함된 옵션 유효성 검사 테스트")
    void checkCorrectEscapeCharacterOption() {
        OptionRequest request = new OptionRequest("[(+-_&/)]", 10);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("수량이 양의 정수가 아닌 옵션 유효성 검사 테스트")
    void checkNotPositiveQuantityOption() {
        OptionRequest request = new OptionRequest("옵션", -1);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("수량이 1억 이상인 옵션 유효성 검사 테스트")
    void checkOneMillionQuantityOption() {
        OptionRequest request = new OptionRequest("옵션", 100_000_000);

        Set<ConstraintViolation<OptionRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

}
