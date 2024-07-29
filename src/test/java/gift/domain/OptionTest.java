package gift.domain;

import gift.exception.OutOfStockException;
import gift.model.Option;
import gift.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OptionTest {
    @Test
    @DisplayName("subtract 성공 테스트")
    void testSubtractSuccess() {
        Product product = new Product();
        Option option = new Option("옵션 테스트", 10L, product);

        option.subtract(5L);

        assertEquals(5L, option.getQuantity());
    }

    @Test
    @DisplayName("quantity 정확히 0의 값으로 만드는 테스트")
    void testSubtractExactQuantity() {
        Product product = new Product();
        Option option = new Option("옵션 테스트", 10L, product);

        option.subtract(10L);

        assertEquals(0, option.getQuantity());
    }

    @Test
    @DisplayName("subtract 수량 부족 테스트")
    public void testSubtract_OutOfStock() {
        Product product = new Product();
        Option option = new Option("옵션 테스트", 5L, product);

        assertThrows(OutOfStockException.class, () -> option.subtract(10L));
    }
}
