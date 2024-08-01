package gift.model;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OptionTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testValidOption() {
        Product product = new Product();
        Option option = new Option("옵션1", 100, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);
        assertTrue(violations.isEmpty(), "No violations expected for a valid option");
    }

    @Test
    public void testOptionNameNotBlank() {
        Product product = new Product();
        Option option = new Option("", 100, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);
        assertEquals(1, violations.size(), "옵션 이름은 필수입니다");
        ConstraintViolation<Option> violation = violations.iterator().next();
        assertEquals("옵션 이름은 필수입니다", violation.getMessage());
    }

    @Test
    public void testOptionNamePattern() {
        Product product = new Product();
        Option option = new Option("Invalid@", 100, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);
        assertEquals(1, violations.size(), "사용 불가능한 특수 문자가 포함되어 있습니다");
        ConstraintViolation<Option> violation = violations.iterator().next();
        assertEquals("사용 불가능한 특수 문자가 포함되어 있습니다", violation.getMessage());
    }

    @Test
    public void testOptionQuantityMin() {
        Product product = new Product();
        Option option = new Option("Valid Option", 0, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);
        assertEquals(1, violations.size(), "수량은 최소 1개 이상이어야 합니다");
        ConstraintViolation<Option> violation = violations.iterator().next();
        assertEquals("수량은 최소 1개 이상이어야 합니다", violation.getMessage());
    }

    @Test
    public void testOptionQuantityMax() {
        Product product = new Product();
        Option option = new Option("Valid Option", 1000000000, product);

        Set<ConstraintViolation<Option>> violations = validator.validate(option);
        assertEquals(1, violations.size(), "수량은 1억개 미만이어야 합니다");
        ConstraintViolation<Option> violation = violations.iterator().next();
        assertEquals("수량은 1억개 미만이어야 합니다", violation.getMessage());
    }
}
