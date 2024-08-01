package gift.EntityTest;

import gift.domain.Option.OptionQuantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OptionQuantityTest {

    @Test
    @DisplayName("정상적인 옵션수량 테스트")
    public void validOptionQuantity_ShouldPass() {
        Long quantity = 50L;
        OptionQuantity optionQuantity = new OptionQuantity(quantity);
        assertThat(optionQuantity.getoptionQuantity()).isEqualTo(quantity);
    }

    @Test
    @DisplayName("1미만 수량 테스트")
    public void validOptionQuantity_ShouldFail_WithQuantityLessThanOne() {
        Long invalidQuantity = 0L;

        assertThatThrownBy(() -> new OptionQuantity(invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 최소 1입니다.");
    }

    @Test
    @DisplayName("1억 이상 수량 테스트")
    public void validOptionQuantity_ShouldFail_WithQuantityGreaterThanOneBillion() {
        Long invalidQuantity = 1_000_000_001L;

        assertThatThrownBy(() -> new OptionQuantity(invalidQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 최대 1억입니다.");
    }

    @Test
    @DisplayName("옵션 수량 빼기 테스트")
    public void subtract_ShouldPass_WithValidSubtraction() throws IllegalAccessException {
        Long initialQuantity = 50L;
        Long subtractQuantity = 10L;
        OptionQuantity optionQuantity = new OptionQuantity(initialQuantity);

        optionQuantity.subtract(subtractQuantity);

        assertThat(optionQuantity.getoptionQuantity()).isEqualTo(initialQuantity - subtractQuantity);
    }

    @Test
    @DisplayName("옵션 수량 뺐을 때 유효성 테스트")
    public void subtract_ShouldFail_WhenResultingQuantityIsLessThanOne() {
        Long initialQuantity = 5L;
        Long subtractQuantity = 10L;
        OptionQuantity optionQuantity = new OptionQuantity(initialQuantity);

        assertThatThrownBy(() -> optionQuantity.subtract(subtractQuantity))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("수량은 최소 1입니다.");
    }

}
