package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.Model.Option;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
public class OptionTest {

    @Test
    void 옵션수량_뺴기_성공(){
        Option option = new Option(1L, null, "test", 100);
        assertThat(option.subtract(10)).isEqualTo(90);
    }

    @Test
    void 옵션수량_뺴기_0이되는경우(){
        Option option = new Option(1L, null, "test", 10);
        assertThat(option.subtract(10)).isEqualTo(0);
    }

    @Test
    void 옵션수량_뺴기_실패_음수(){
        Option option = new Option(1L, null, "test", 100);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            option.subtract(-1);
        });

    }

    @Test
    void 옵션수량_뺴기_실패_뺼수없음(){
        Option option = new Option(1L, null, "test", 3);
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            option.subtract(5);
        });
    }

}
