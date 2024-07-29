package gift.model;

import static gift.util.constants.OptionConstants.INSUFFICIENT_QUANTITY;
import static gift.util.constants.OptionConstants.NAME_INVALID_CHARACTERS;
import static gift.util.constants.OptionConstants.NAME_SIZE_LIMIT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OptionTest {

    private static Validator validator;

    @BeforeAll
    public static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("유효하지 않은 옵션 이름 - 길이 초과")
    public void testInvalidOptionNameSize() {
        Product product = new Product();
        String invalidName = "가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하가나다라마바사아자차카타파하";
        Option option = new Option(1L, invalidName, 100, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(NAME_SIZE_LIMIT)
        );
    }

    @Test
    @DisplayName("유효하지 않은 옵션 이름 - 특수 문자 포함")
    public void testInvalidOptionNamePattern() {
        Product product = new Product();
        Option option = new Option(1L, "Invalid@Name!", 100, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(violation ->
            violation.getPropertyPath().toString().equals("name") &&
                violation.getMessage().equals(NAME_INVALID_CHARACTERS)
        );
    }

    @Test
    @DisplayName("옵션 수량 차감 - 유효한 경우")
    public void testSubtractQuantityValid() {
        Product product = new Product();
        Option option = new Option(1L, "ValidOption", 100, product);

        option.subtractQuantity(10);

        assertThat(option.getQuantity()).isEqualTo(90);
    }

    @Test
    @DisplayName("옵션 수량 차감 - 수량 부족")
    public void testSubtractQuantityInsufficient() {
        Product product = new Product();
        Option option = new Option(1L, "ValidOption", 5, product);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            option.subtractQuantity(10);
        });

        assertThat(exception.getMessage()).contains(INSUFFICIENT_QUANTITY + 1);
    }
}
