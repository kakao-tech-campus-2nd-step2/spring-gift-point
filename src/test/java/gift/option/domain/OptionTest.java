package gift.option.domain;

import gift.option.exception.OptionNotEnoughException;
import gift.product.domain.Product;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionTest {

    @Test
    void testOptionEqualsAndHashCode_ById() {
        OptionName optionName1 = new OptionName("Name1");
        OptionName optionName2 = new OptionName("Name2");
        OptionCount optionCount1 = new OptionCount(10L);
        OptionCount optionCount2 = new OptionCount(20L);
        Product product = new Product(); // assuming a default constructor for simplicity

        Option option1 = new Option(1L, optionName1, optionCount1, product);
        Option option2 = new Option(1L, optionName2, optionCount2, product);
        Option option3 = new Option(2L, optionName1, optionCount1, product);

        assertThat(option1).isEqualTo(option2);
        assertThat(option1).isNotEqualTo(option3);
        assertThat(option1.hashCode()).isEqualTo(option2.hashCode());
        assertThat(option1.hashCode()).isNotEqualTo(option3.hashCode());
    }

    @Test
    void testSubtract_Success() {
        OptionName optionName = new OptionName("Option1");
        OptionCount optionCount = new OptionCount(10L);
        Product product = new Product();

        Option option = new Option(1L, optionName, optionCount, product);
        option.subtract(5);

        assertThat(option.getCount().getOptionCountValue()).isEqualTo(5L);
    }

    @Test
    void testSubtract_Failure() {
        OptionName optionName = new OptionName("Option1");
        OptionCount optionCount = new OptionCount(10L);
        Product product = new Product();

        Option option = new Option(1L, optionName, optionCount, product);

        assertThatThrownBy(() -> option.subtract(15))
                .isInstanceOf(OptionNotEnoughException.class);
    }
}
