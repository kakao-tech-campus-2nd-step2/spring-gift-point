package gift;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.model.OptionQuantity;
import org.junit.jupiter.api.Test;

public class OptionQuantityTest {

    @Test
    void testValidQuantity() {
        OptionQuantity optionQuantity = new OptionQuantity(100);
        assertEquals(100, optionQuantity.getQuantity());
    }

    @Test
    void testQuantityBelowMin() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new OptionQuantity(0);
        });
        assertEquals("옵션 수량은 1개 이상 1억 개 미만이어야 합니다.", exception.getMessage());
    }

    @Test
    void testQuantityAboveMax() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new OptionQuantity(100000000);
        });
        assertEquals("옵션 수량은 1개 이상 1억 개 미만이어야 합니다.", exception.getMessage());
    }

    @Test
    void testDecreaseQuantity() {
        OptionQuantity optionQuantity = new OptionQuantity(10);
        optionQuantity = new OptionQuantity(Math.max(1, optionQuantity.getQuantity() - 5));
        assertEquals(5, optionQuantity.getQuantity());

        optionQuantity = new OptionQuantity(Math.max(1, optionQuantity.getQuantity() - 10));
        assertEquals(1, optionQuantity.getQuantity());
    }
}