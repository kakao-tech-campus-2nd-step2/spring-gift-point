package gift.dto;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OptionTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 입력 값이 유효한 케이스")
    void testOptionSuccess() {
        OptionDTO optionDTO = new OptionDTO("option_name", 100);
        Set<ConstraintViolation<OptionDTO>> violations = validator.validate(optionDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("비어있는 옵션명")
    void testNullOptionName() {
        OptionDTO optionDTO = new OptionDTO(null, 100);
        Set<ConstraintViolation<OptionDTO>> violations = validator.validate(optionDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("옵션명은 필수 항목입니다.");
    }

    @Test
    @DisplayName("1자 이하의 옵션명")
    void testTooShortOptionName() {
        OptionDTO optionDTO = new OptionDTO("", 100);
        Set<ConstraintViolation<OptionDTO>> violations = validator.validate(optionDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("옵션의 이름은 최소 1자부터 최대 50자 미만입니다.");
    }

    @Test
    @DisplayName("50자 이상의 옵션명")
    void testTooLongtOptionName() {
        OptionDTO optionDTO = new OptionDTO("option_name".repeat(5), 100);
        Set<ConstraintViolation<OptionDTO>> violations = validator.validate(optionDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("옵션의 이름은 최소 1자부터 최대 50자 미만입니다.");
    }

    @Test
    @DisplayName("1개 이하의 수량")
    void testMinOptionName() {
        OptionDTO optionDTO = new OptionDTO("option", -100);
        Set<ConstraintViolation<OptionDTO>> violations = validator.validate(optionDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("수량은 최소 1개 이상, 최대 1억개 미만입니다.");
    }

    @Test
    @DisplayName("1억개 이상의 수량")
    void testMaxOptionName() {
        OptionDTO optionDTO = new OptionDTO("option", 100_000_100);
        Set<ConstraintViolation<OptionDTO>> violations = validator.validate(optionDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("수량은 최소 1개 이상, 최대 1억개 미만입니다.");
    }
}
