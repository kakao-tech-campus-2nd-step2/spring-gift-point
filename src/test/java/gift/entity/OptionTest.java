package gift.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OptionTest {

    @Test
    void optionConstructorAndGetters() {
        Product product = new Product();
        Option option = new Option("Test Option", 100, product);

        assertThat(option.getName()).isEqualTo("Test Option");
        assertThat(option.getQuantity()).isEqualTo(100);
        assertThat(option.getProduct()).isEqualTo(product);
    }

    @Test
    void optionSetters() {
        Option option = new Option();
        Product product = new Product();
        option.setName("New Option");
        option.setQuantity(200);
        option.setProduct(product);

        assertThat(option.getName()).isEqualTo("New Option");
        assertThat(option.getQuantity()).isEqualTo(200);
        assertThat(option.getProduct()).isEqualTo(product);
    }

    @Test
    void subtractQuantity() {
        Option option = new Option("Test Option", 100, new Product());
        option.subtractQuantity(50);

        assertThat(option.getQuantity()).isEqualTo(50);
    }

    @Test
    void subtractQuantityExceedsAvailable() {
        Option option = new Option("Test Option", 100, new Product());

        assertThrows(IllegalArgumentException.class, () -> {
            option.subtractQuantity(150);
        });
    }
}