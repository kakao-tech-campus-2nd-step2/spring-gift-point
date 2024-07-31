package gift.entity;

import gift.entity.option.Option;
import gift.entity.option.OptionDTO;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class OptionTest {

    @Test
    void option_quantity_subtract_test() {
        // given
        int total = 100;
        int amount = 30;
        Option option = new Option(new OptionDTO("test", total));

        // when
        option.subtract(amount);

        // then
        assertThat(option.getQuantity()).isEqualTo(total - amount);
    }
}
