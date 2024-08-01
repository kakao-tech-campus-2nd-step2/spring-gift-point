package gift.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class OrderTest {

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("모든 입력 값이 유효한 경우")
    public void testValidOrderDTO() {
        OrderDTO orderDTO = new OrderDTO(1L, 2, "Happy Birthday!");

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(orderDTO);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("옵션 ID가 없는 경우")
    public void testOrderDTOWithoutOptionId() {
        OrderDTO orderDTO = new OrderDTO(null, 2, "Happy Birthday!");

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(orderDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("옵션 id는 필수 입력 사항입니다.");
    }

    @Test
    @DisplayName("수량이 없는 경우")
    public void testOrderDTOWithoutQuantity() {
        OrderDTO orderDTO = new OrderDTO(1L, 0, "Thank you!");

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(orderDTO);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("수량은 1 이상의 숫자여야 합니다.");
    }

    @Test
    @DisplayName("메시지가 없는 경우")
    public void testOrderDTOWithoutMessage() {
        OrderDTO orderDTO = new OrderDTO(1L, 2, null);

        Set<ConstraintViolation<OrderDTO>> violations = validator.validate(orderDTO);

        assertThat(violations).isEmpty();
    }
}
