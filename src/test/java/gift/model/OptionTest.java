package gift.model;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OptionTest {

    private Option option;
    private Category category;
    private Product product;

    @BeforeEach
    void setUp() {
        category = new Category(null, "교환권");
        product = new Product(1L, "kbm", "100", category, "https://kakao");
        option = new Option(3L, "임시옵션", 100L, product);
    }

    @Test
    void testCreateWithNullName() {
        try {
            option = new Option(null, null, 1L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithEmptyName() {
        try {
            option = new Option(null, "", 1L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithLengthName() {
        try {
            option = new Option(null, "abcde".repeat(300), 1L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithInvalidName() {
        try {
            option = new Option(null, "<>\\.", 1L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWithNullQuantity() {
        try {
            option = new Option(null, "임시옵션", null, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWith0Quantity() {
        try {
            option = new Option(null, "임시옵션", 0L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWith0LessQuantity() {
        try {
            option = new Option(null, "임시옵션", -100L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateWith1MillionGreaterQuantity() {
        try {
            option = new Option(null, "임시옵션", 100_000_000L, product);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testCreateValidOption() {
        assertAll(
            () -> assertThat(option.getId()).isNotNull(),
            () -> assertThat(option.getName()).isEqualTo("임시옵션"),
            () -> assertThat(option.getQuantity()).isEqualTo(100L),
            () -> assertThat(option.getProduct().getId()).isNotNull()
        );
    }

    @Test
    void testUpdateWithValidNameValidQuantity() {
        option.updateOption("테스트",10L);
        assertThat("테스트").isEqualTo(option.getName());
        assertEquals(10L,option.getQuantity());
    }

    @Test
    void testUpdateWithValidNameNullQuantity() {
        try {
            option.updateOption("테스트", null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithValidNameZeroQuantity() {
        try {
            option.updateOption("테스트", 0L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithValidNameMinusQuantity() {
        try {
            option.updateOption("테스트", -10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithValidNameMillionQuantity() {
        try {
            option.updateOption("테스트", 100_000_000L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithNullNameValidQuantity() {
        try {
            option.updateOption(null, 10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithNullNameNullQuantity() {
        try {
            option.updateOption(null, null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithNullNameZeroQuantity() {
        try {
            option.updateOption(null, 0L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithNullNameMinusQuantity() {
        try {
            option.updateOption(null, -10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithNullNameMillionQuantity() {
        try {
            option.updateOption(null, 100_000_000L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithEmptyNameValidQuantity() {
        try {
            option.updateOption("", 10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithEmptyNameNullQuantity() {
        try {
            option.updateOption("", null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithEmptyNameZeroQuantity() {
        try {
            option.updateOption("", 0L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithEmptyNameMinusQuantity() {
        try {
            option.updateOption("", -10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithEmptyNameMillionQuantity() {
        try {
            option.updateOption("", 100_000_000L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithLengthNameValidQuantity() {
        try {
            option.updateOption("test".repeat(300), 10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithLengthNameNullQuantity() {
        try {
            option.updateOption("test".repeat(300), null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithLengthNameZeroQuantity() {
        try {
            option.updateOption("test".repeat(300), 0L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithLengthNameMinusQuantity() {
        try {
            option.updateOption("test".repeat(300), -10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithLengthNameMillionQuantity() {
        try {
            option.updateOption("test".repeat(300), 100_000_000L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithInvalidNameValidQuantity() {
        try {
            option.updateOption(".<>", 10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithInvalidNameNullQuantity() {
        try {
            option.updateOption(".<>", null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithInvalidNameZeroQuantity() {
        try {
            option.updateOption(".<>", 0L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithInvalidNameMinusQuantity() {
        try {
            option.updateOption(".<>", -10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testUpdateWithInvalidNameMillionQuantity() {
        try {
            option.updateOption(".<>", 100_000_000L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testSubtractQuantityWithNull() {
        try {
            option.subtractQuantity(null);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testSubtractQuantityWithZero() {
        try {
            option.subtractQuantity(0L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testSubtractQuantityWith0Less() {
        try {
            option.subtractQuantity(-10L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testSubtractQuantityMoreThanQuantity() {
        try {
            option.subtractQuantity(200L);
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    void testValidSubtractQuantity() {
        option.subtractQuantity(50L);
        assertEquals(50L, option.getQuantity());
    }
}