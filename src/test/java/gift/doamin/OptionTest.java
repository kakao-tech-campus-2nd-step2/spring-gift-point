package gift.doamin;

import gift.domain.Option;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class OptionTest {
    @Test
    void optionTest(){
        Option option = new Option("[1] 아시아", 23L);

        Assertions.assertThat(option).isNotNull();

        option.update("[1] 아메리카", 22L);
        Assertions.assertThat(option.getName()).isEqualTo("[1] 아메리카");
        Assertions.assertThat(option.getQuantity()).isEqualTo(22L);

        option.subtract(10L);
        Assertions.assertThat(option.getQuantity()).isEqualTo(12L);
    }
}
