package gift.option.domain;

import gift.global.response.ErrorCode;
import gift.option.exception.OptionValidException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OptionNameTest {

    @Test
    void createValidOptionName() {
        OptionName optionName = new OptionName("Valid ( ) [ ] + - & / _ Name");
        assertThat(optionName.getOptionNameValue()).isEqualTo("Valid ( ) [ ] + - & / _ Name");
    }

    @Test
    void createOptionName_withNullValue_shouldThrowException() {
        assertThatThrownBy(() -> new OptionName(null))
                .isInstanceOf(OptionValidException.class)
                .hasMessageContaining(ErrorCode.OPTION_NAME_LENGTH_ERROR.getMessage());
    }

    @Test
    void createOptionName_withEmptyValue_shouldThrowException() {
        assertThatThrownBy(() -> new OptionName(""))
                .isInstanceOf(OptionValidException.class)
                .hasMessageContaining(ErrorCode.OPTION_NAME_LENGTH_ERROR.getMessage());
    }

    @Test
    void createOptionName_withTooLongValue_shouldThrowException() {
        String longName = "a".repeat(OptionName.MAX_LENGTH + 1);
        assertThatThrownBy(() -> new OptionName(longName))
                .isInstanceOf(OptionValidException.class)
                .hasMessageContaining(ErrorCode.OPTION_NAME_LENGTH_ERROR.getMessage());
    }

    @Test
    void createOptionName_withInvalidCharacters_shouldThrowException() {
        String[] invalidCharacters = {"!", "\"", "#", "$", "%", "'", "*", ",", ".", ":", ";", "<", "=", ">", "?", "@", "\\", "^", "`", "{", "|", "}", "~"};

        for (String invalidChar : invalidCharacters) {
            assertThatThrownBy(() -> new OptionName("Invalid" + invalidChar + "Name"))
                    .isInstanceOf(OptionValidException.class)
                    .hasMessageContaining(ErrorCode.OPTION_NAME_PATTER_ERROR.getMessage());
        }
    }
}