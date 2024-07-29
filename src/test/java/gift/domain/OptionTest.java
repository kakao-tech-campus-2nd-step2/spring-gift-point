package gift.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import gift.domain.option.entity.Option;
import gift.domain.option.exception.OptionNameValidException;
import gift.domain.option.exception.OptionQuantityValidException;
import gift.domain.option.exception.OptionValidException;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class OptionTest {

    @Test
    @DisplayName("이름이 중복일 경우 테스트")
    void checkDuplicateNameTest() {
        List<Option> optionList = Arrays.asList(createOption("test1"), createOption("test2"));
        Option newOption = createOption("test1");

        assertThatThrownBy(() -> newOption.checkDuplicateName(optionList)).isInstanceOf(
            OptionNameValidException.class);
    }

    @Test
    @DisplayName("이름이 중복이 아닐 경우 테스트")
    void checkNotDuplicateNameTest() {
        List<Option> optionList = Arrays.asList(createOption("test1"), createOption("test2"));
        Option newOption = createOption("test3");

        assertDoesNotThrow(() -> newOption.checkDuplicateName(optionList));
    }

    @Test
    @DisplayName("옵션 이름 길이 초과 검사")
    void optionNameLengthFailTest() {
        assertThatThrownBy(() -> {
            createOption("name".repeat(50));
        }).isInstanceOf(OptionNameValidException.class)
            .hasMessage("옵션 이름 50자 초과");
    }

    @Test
    @DisplayName("정상 옵션 이름 길이 검사")
    void optionNameLengthSuccessTest() {
        assertDoesNotThrow(() -> {
            createOption("name");
        });
    }

    @Test
    @DisplayName("수량 1억개 이상일 경우 테스트")
    void optionQuantityExceedTest() {
        assertThatThrownBy(() -> {
            createOption(100_000_000);
        }).isInstanceOf(OptionQuantityValidException.class)
            .hasMessage("수량은 1개 이상 1억개 미만으로 설정해주세요.");
    }

    @Test
    @DisplayName("수량 1개 미만일 경우 테스트")
    void optionQuantityLackTest() {
        assertThatThrownBy(() -> {
            createOption(0);
        }).isInstanceOf(OptionQuantityValidException.class)
            .hasMessage("수량은 1개 이상 1억개 미만으로 설정해주세요.");
    }

    @Test
    @DisplayName("수량 정상 테스트")
    void optionQuantitySuccessTest() {
        assertDoesNotThrow(() -> {
            createOption(100);
        });
    }

    @Test
    @DisplayName("수량 빼기 테스트")
    void subtractTest() {
        int subtract = 100;

        Option option = createOption(1000);
        option.subtractQuantity(subtract);

        assertThat(option.getQuantity()).isEqualTo(900);
    }

    @Test
    @DisplayName("허용되지 않는 문자 테스트")
    void notAllowCharacterTest() {
        assertThatThrownBy(() -> {
            createOption("#@!#허용되지않는문자");
        }).isInstanceOf(OptionValidException.class)
            .hasMessage("특수 문자는 '(), [], +, -, &, /, _ '만 사용가능 합니다.");
    }

    @Test
    @DisplayName("허용되는 문자 테스트")
    void allowCharacterTest() {
        assertDoesNotThrow(() -> {
            createOption("허용되는 문자");
        });
    }

    private Option createOption(int quantity) {
        return createOption("test", quantity);
    }

    private Option createOption(String name) {
        return createOption(name, 1000);
    }

    private Option createOption(String name, int quantity) {
        return new Option(name, quantity);
    }
}
