package gift.doamin.product.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import org.junit.jupiter.api.Test;

class OptionTest {

    @Test
    void 현재_수량보다_많은_값을_차감하는_경우() {
        int quantity = 10;
        Option option = new Option("옵션", quantity);

        assertThatIllegalArgumentException()
            .isThrownBy(() -> option.subtract(quantity + 1));
    }

    @Test
    void 현재_수량과_같은_값을_차감하는_경우() {
        int quantity = 10;
        Option option = new Option("옵션", quantity);

        option.subtract(quantity);

        assertThat(option.getQuantity()).isZero();
    }

    @Test
    void 현재_수량보다_적은_값을_차감하는_경우() {
        int quantity = 10;
        Option option = new Option("옵션", quantity);

        option.subtract(quantity - 1);

        assertThat(option.getQuantity()).isPositive();
    }
}