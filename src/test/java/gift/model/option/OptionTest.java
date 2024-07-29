package gift.model.option;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OptionTest {

    private Option option;

    @BeforeEach
    void setUp() {
        option = new Option("Test Option", 10);
    }

    @Test
    @DisplayName("수량 차감이 정상적으로 동작하는지 테스트")
    void testSubtractQuantity() {
        option.subtract(5);
        assertEquals(5, option.getQuantity());
    }

    @Test
    @DisplayName("수량 차감시 수량이 부족하면 예외가 발생하는지 테스트")
    void testSubtractQuantityInsufficient() {
       assertThrows(IllegalArgumentException.class, () -> {
            option.subtract(15);
        });
    }
}