package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class OptionTest {
    private Option option;

    @BeforeEach
    public void setUp() {
        Category category = new Category("category", "color", "image", "");
        Product product = new Product(5L, "Test Product", 100, "image.jpg", category);
        option = new Option("Test Option", 10, product);
    }

    @Test
    public void testSubtract_ValidAmount() {
        int amount = 5;

        option.subtract(amount);

        assertThat(option.getQuantity()).isEqualTo(5);
    }

    @Test
    public void testSubtract_NegativeAmount() {
        int amount = -5;

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> option.subtract(amount))
            .withMessage("뺄 값은 양수만 가능하다.");
    }

    @Test
    public void testSubtract_GreaterAmount() {
        int amount = 15;

        assertThatExceptionOfType(IllegalStateException.class)
            .isThrownBy(() -> option.subtract(amount))
            .withMessage("뺄 값은 기존 quantity보다 작아야 한다.");
    }
}
