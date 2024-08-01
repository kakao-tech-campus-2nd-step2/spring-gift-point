package gift.domain.option;

import gift.exception.CustomException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OptionTest {
    @Test
    void 옵션_차감_성공() {
        //given
        Option option = new Option("테스트", 100);
        //when
        option.subtract(70);
        //then
        assertThat(option.getQuantity()).isEqualTo(30);
    }

    @Test
    void 옵션_차감_실패() {
        //given
        Option option = new Option("테스트", 100);
        //when
        //then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> option.subtract(110));
    }
}
