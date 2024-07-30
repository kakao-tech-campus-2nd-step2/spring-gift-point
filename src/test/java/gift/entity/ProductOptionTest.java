package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductOptionTest {

    @Test
    void 생성_성공() {
        Product product = new Product();
        Option option = new Option();

        ProductOption productOption = new ProductOption(product, option, 10);

        assertNotNull(productOption);
        assertEquals(product, productOption.getProduct());
        assertEquals(option, productOption.getOption());
        assertEquals(10, productOption.getQuantity());
    }

    @Test
    void 생성_실패_수량_음수() {
        Product product = new Product();
        Option option = new Option();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductOption(product, option, -1);
        });

        assertEquals(ErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 생성_실패_수량_0() {
        Product product = new Product();
        Option option = new Option();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductOption(product, option, 0);
        });

        assertEquals(ErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 생성_실패_수량_최대값_초과() {
        Product product = new Product();
        Option option = new Option();

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new ProductOption(product, option, 100000000);
        });

        assertEquals(ErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 수량_업데이트_성공() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        productOption.updateQuantity(20);

        assertEquals(20, productOption.getQuantity());
    }

    @Test
    void 수량_업데이트_실패_음수() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOption.updateQuantity(-1);
        });

        assertEquals(ErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 수량_업데이트_실패_0() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOption.updateQuantity(0);
        });

        assertEquals(ErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 수량_업데이트_실패_최대값_초과() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOption.updateQuantity(100000000);
        });

        assertEquals(ErrorCode.INVALID_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 수량_감소_성공() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        productOption.decreaseQuantity(5);

        assertEquals(5, productOption.getQuantity());
    }

    @Test
    void 수량_감소_실패_음수() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOption.decreaseQuantity(-1);
        });

        assertEquals(ErrorCode.INVALID_DECREASE_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 수량_감소_실패_0() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOption.decreaseQuantity(0);
        });

        assertEquals(ErrorCode.INVALID_DECREASE_QUANTITY, exception.getErrorCode());
    }

    @Test
    void 수량_감소_실패_수량부족() {
        Product product = new Product();
        Option option = new Option();
        ProductOption productOption = new ProductOption(product, option, 10);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            productOption.decreaseQuantity(15);
        });

        assertEquals(ErrorCode.INSUFFICIENT_QUANTITY, exception.getErrorCode());
    }
}
