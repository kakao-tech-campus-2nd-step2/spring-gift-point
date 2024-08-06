package gift.dto;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.option.OptionRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OptionRequestTest {

    private static ValidatorFactory factory;
    private static Validator validator;

    private Set<ConstraintViolation<OptionRequest>> violations;

    @BeforeEach
    public void setUp() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterEach
    public void print() {
        for (ConstraintViolation<OptionRequest> violation : violations) {
            System.out.println(violation.getMessage());
        }
    }

    @Test
    @DisplayName("정상: OptionRequest")
    public void OptionRequest_정상() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 1L);
        violations = validator.validate(optionRequest);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("예외: OptionRequest 빈 이름")
    public void OptionRequest_빈_이름() {
        OptionRequest optionRequest = new OptionRequest("", 1L);
        violations = validator.validate(optionRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: OptionRequest 이름 50자 초과")
    public void OptionRequest_이름_50자_초과() {
        OptionRequest optionRequest = new OptionRequest("a".repeat(51), 1L);
        violations = validator.validate(optionRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: OptionRequest 옵션명 사용불가 특수문자")
    public void OptionRequest_옵션명_사용불가_특수문자() {
        OptionRequest optionRequest = new OptionRequest("*", 1L);
        violations = validator.validate(optionRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: OptionRequest 빈 수량")
    public void OptionRequest_빈_수량() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", null);
        violations = validator.validate(optionRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: OptionRequest 수량 음수")
    public void OptionRequest_수량_음수() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", -1L);
        violations = validator.validate(optionRequest);

        assertThat(violations).isNotEmpty();
    }

    @Test
    @DisplayName("예외: OptionRequest 수량 1억 초과")
    public void OptionRequest_수량_1억_초과() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 100000001L);
        violations = validator.validate(optionRequest);

        assertThat(violations).isNotEmpty();
    }
}
