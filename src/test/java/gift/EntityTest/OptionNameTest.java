package gift.EntityTest;

import gift.domain.OptionDomain.OptionName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class OptionNameTest {
    @Test
    @DisplayName("유효한 옵션명 테스트")
    public void testValidOptionName() {
        OptionName optionName = new OptionName("ValidName");

        assertDoesNotThrow(() -> optionName.validOptionName("ValidName123"));
        assertDoesNotThrow(() -> optionName.validOptionName("유효한이름"));
        assertDoesNotThrow(() -> optionName.validOptionName("Valid Name"));
    }
    
    @Test
    @DisplayName("허용되지 않는 특수기호 옵션명 테스트")
    public void testOptionNameContainsInvalidCharacters() {
        OptionName OptionName = new OptionName();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            OptionName.validOptionName("Invalid@Name");
        });
        assertEquals(" ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다.", thrown.getMessage());
    }
}
