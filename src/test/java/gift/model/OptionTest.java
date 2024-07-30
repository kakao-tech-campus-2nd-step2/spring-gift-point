package gift.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import gift.common.exception.OptionException;
import gift.option.model.Option;
import gift.product.model.Product;
import java.util.List;
import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void updateInfo_중복이_없는_경우() {
        Product product = new Product("product", 1000, "product.jpg", null);
        Option option = new Option("test", 1, product);
        String name = "changed";
        Integer quantity = 2;
        option.updateInfo(name, quantity);

        assertThat(option.getName()).isEqualTo(name);
        assertThat(option.getQuantity()).isEqualTo(quantity);
    }

    @Test
    void updateInfo_중복이_있는_경우() {
        Product product = new Product("product", 1000, "product.jpg", null);
        Option option = new Option("changed", 3, product);
        Option option1 = new Option("test", 1, product);
        String name = "changed";
        Integer quantity = 2;

        assertThatExceptionOfType(OptionException.class).isThrownBy(
            () -> option1.updateInfo(name, quantity));
    }

    @Test
    void validateOptionCount() {
        Product product = new Product("product", 1000, "product.jpg", null);
        assertThatExceptionOfType(OptionException.class).isThrownBy(
            () -> Option.Validator.validateOptionCount(List.of(new Option("test", 1, product)))
        );
    }

    @Test
    void validateDuplicated() {
        Product product = new Product("product", 1000, "product.jpg", null);
        new Option("test", 1, product);
        assertThatExceptionOfType(OptionException.class).isThrownBy(
            () -> new Option("test", 2, product)
        );
    }
}
