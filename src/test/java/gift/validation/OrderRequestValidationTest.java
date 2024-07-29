package gift.validation;

import gift.order.dto.OrderRequest;
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

public class OrderRequestValidationTest {

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
    @DisplayName("정상 주문 유효성 검사 테스트")
    void checkNormalOrder() {
        OrderRequest request = new OrderRequest(1L, 1, "message");

        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("메시지가 빈 주문 유효성 검사 테스트")
    void checkEmptyMessageOrder() {
        OrderRequest request = new OrderRequest(1L, 1, "");

        Set<ConstraintViolation<OrderRequest>> violations = validator.validate(request);

        Assertions.assertThat(violations).isNotEmpty();
    }

}
