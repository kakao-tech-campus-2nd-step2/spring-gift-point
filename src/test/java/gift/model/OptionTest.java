package gift.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class OptionTest {

    @Test
    @DisplayName("상품 옵션의 수량을 지정된 숫자만큼 빼는 테스트[성공]")
    void subTractQuantity() {
        // given
        int quantity = 10;
        int subtractionAmount = 1;
        int expectedQuantity = quantity - subtractionAmount;
        Option option = new Option("oName", quantity);

        // when
        int actual = option.subtractQuantity(subtractionAmount);

        // then
        assertThat(actual).isEqualTo(expectedQuantity);
    }

    @Test
    @DisplayName("상품 옵션의 수량을 지정된 숫자만큼 빼는 테스트[실패] - 수량 초과")
    void subTractAmountExceedQuantity() {
        // given
        int quantity = 10;
        int subtractionAmount = quantity + 1;
        Option option = new Option("oName", quantity);

        // when
        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> option.subtractQuantity(subtractionAmount));
    }
}