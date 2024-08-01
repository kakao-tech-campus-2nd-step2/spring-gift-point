package gift.doamin;

import gift.domain.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionTest {
    @Test
    void optionTest(){
        Option option = new Option("[1] 아시아", 23);

        Assertions.assertThat(option).isNotNull();

        option.update("[1] 아메리카", 22);
        Assertions.assertThat(option.getName()).isEqualTo("[1] 아메리카");
        Assertions.assertThat(option.getQuantity()).isEqualTo(22);

        option.subtract(10);
        Assertions.assertThat(option.getQuantity()).isEqualTo(12);
    }
}
