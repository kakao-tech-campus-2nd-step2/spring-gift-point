package gift.product;

import gift.core.domain.product.ProductOption;
import gift.core.exception.validation.InvalidArgumentException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class ProductOptionTests {
    @Test
    void testValidateOrderable() {
        ProductOption productOption = ProductOption.of("test", 10);

        assertThatCode(()->
            productOption.validateOrderable(5)
        ).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("주문 가능 수량이 0보다 작을 때 예외가 발생한다.")
    void testValidateOrderableWhenQuantityIsNegative() {
        ProductOption productOption = ProductOption.of("test", -10);

        assertThatThrownBy(
                ()-> productOption.validateOrderable(5)
        ).isInstanceOf(InvalidArgumentException.class);
    }

    @Test
    @DisplayName("주문 가능 수량이 재고 수량보다 클 때 예외가 발생한다.")
    void testValidateOrderableWhenQuantityIsGreaterThanStock() {
        ProductOption productOption = ProductOption.of("test", 10);

        assertThatThrownBy(
                ()-> productOption.validateOrderable(15)
        ).isInstanceOf(InvalidArgumentException.class);
    }
}
