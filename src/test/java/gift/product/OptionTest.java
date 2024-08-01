package gift.product;

import gift.model.product.Option;
import gift.model.product.Product;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;


public class OptionTest {

    @Mock
    private Product product;
    @Test
    void isProductEnough(){
        Option option = new Option(product,"option1",100);
        assertTrue(option.isProductEnough(50));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            option.isProductEnough(150); // 구매 수량이 150일 때 예외 발생 확인
        });
        assertEquals("Not enough product available", exception.getMessage());
    }
}
