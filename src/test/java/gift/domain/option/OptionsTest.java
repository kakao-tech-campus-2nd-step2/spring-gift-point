package gift.domain.option;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OptionsTest {
    @Test
    void 같은_상품의_옵션_이름이_중복되는_경우() {
        String DuplicatedName = "이것은 중복되는 이름이다!";
        var options = new Options(List.of(new Option(DuplicatedName, 1L, null)));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> options.validate(new Option(DuplicatedName, 1L, null)));
    }

    @Test
    void 옵션_추가() {
        var options = new Options(List.of(new Option("option1", 1L, null)));
        Assertions.assertThat(options.toList()).hasSize(1);

        options.validate(new Option("option2", 1L, null));
        Assertions.assertThat(options.toList()).hasSize(2);
    }
}