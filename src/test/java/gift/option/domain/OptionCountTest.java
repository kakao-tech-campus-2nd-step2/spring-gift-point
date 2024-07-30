package gift.option.domain;

import gift.global.response.ErrorCode;
import gift.option.exception.OptionValidException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionCountTest {
    @Test
    void createValidOptionCount() {
        OptionCount optionCount = new OptionCount(10L);
        assertThat(optionCount.getOptionCountValue()).isEqualTo(10L);
    }

    @Test
    void createOptionCount_withNullValue_shouldThrowException() {
        assertThatThrownBy(() -> new OptionCount(null))
                .isInstanceOf(OptionValidException.class)
                .hasMessageContaining(ErrorCode.OPTION_COUNT_OUTBOUND_ERROR.getMessage());
    }

    @Test
    void createOptionCount_withLowerBound_shouldThrowException() {
        assertThatThrownBy(() -> new OptionCount(0L))
                .isInstanceOf(OptionValidException.class)
                .hasMessageContaining(ErrorCode.OPTION_COUNT_OUTBOUND_ERROR.getMessage());
    }

    @Test
    void createOptionCount_withUpperBound_shouldThrowException() {
        assertThatThrownBy(() -> new OptionCount(OptionCount.MAX_COUNT))
                .isInstanceOf(OptionValidException.class)
                .hasMessageContaining(ErrorCode.OPTION_COUNT_OUTBOUND_ERROR.getMessage());
    }
}