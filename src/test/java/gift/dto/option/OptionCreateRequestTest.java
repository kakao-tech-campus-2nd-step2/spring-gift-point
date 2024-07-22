package gift.dto.option;

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

public class OptionCreateRequestTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효한 옵션 추가")
    public void testAddOptionValid() {
        OptionCreateRequest optionCreateRequest = new OptionCreateRequest("Valid Option", 100);

        Set<ConstraintViolation<OptionCreateRequest>> violations = validator.validate(
            optionCreateRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("필수 필드 누락 - 이름")
    public void testAddOptionNameMissing() {
        OptionCreateRequest optionCreateRequest = new OptionCreateRequest(null, 100);

        Set<ConstraintViolation<OptionCreateRequest>> violations = validator.validate(
            optionCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }

    @Test
    @DisplayName("필수 필드 누락 - 수량")
    public void testAddOptionQuantityMissing() {
        OptionCreateRequest optionCreateRequest = new OptionCreateRequest("Valid Option", null);

        Set<ConstraintViolation<OptionCreateRequest>> violations = validator.validate(
            optionCreateRequest);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("quantity") &&
                violation.getMessage().equals(REQUIRED_FIELD_MISSING)
        );
    }
}
