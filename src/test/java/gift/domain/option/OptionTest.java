package gift.domain.option;

import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OptionTest {
    @Test
    void 이름이_50자_이상인_경우() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Option("option".repeat(50), 1L, null));

    }

    @Test
    void 옵션_수량이_0보다_작은_경우() {
        assertAll(
            () -> Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Option("option", 0L, null)),
            () -> Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> new Option("option", -1L, null))
            );
    }

    @Test
    void 옵션_수량이_1억보다_큰_경우() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> new Option("option", 100_000_000L, null));
    }

    @Test
    void 빼는_수량이_1보다_작을_경우() {
        Long subQuantity = -20L;
        var option = new Option("option", 1L, null);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> option.subtractQuantity(subQuantity));
    }

    @Test
    void 수량보다_더_큰_숫자로_빼는_경우() {
        Long testQuantity = 50L;
        Long subQuantity = 100L;
        var option = new Option("option", testQuantity, null);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> option.subtractQuantity(subQuantity));
    }
}