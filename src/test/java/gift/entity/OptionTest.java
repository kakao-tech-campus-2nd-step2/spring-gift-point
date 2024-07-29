package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionTest {

    @Test
    void 옵션_생성_성공() {
        OptionName optionName = new OptionName("옵션 이름");
        Option option = new Option(optionName);

        assertNotNull(option);
        assertEquals("옵션 이름", option.getName().getValue());
    }

    @Test
    void 옵션_업데이트_성공() {
        OptionName optionName = new OptionName("옵션 이름");
        Option option = new Option(optionName);

        OptionName newOptionName = new OptionName("새 옵션 이름");
        option.update(newOptionName);

        assertEquals("새 옵션 이름", option.getName().getValue());
    }

    @Test
    void 옵션_이름_성공() {
        OptionName optionName = new OptionName("옵션 이름");

        assertNotNull(optionName);
        assertEquals("옵션 이름", optionName.getValue());
    }

    @Test
    void 옵션_이름_실패_null() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new OptionName(null);
        });

        assertEquals(ErrorCode.INVALID_OPTION_NAME, exception.getErrorCode());
    }

    @Test
    void 옵션_이름_실패_빈값() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new OptionName("");
        });

        assertEquals(ErrorCode.INVALID_OPTION_NAME, exception.getErrorCode());
    }

    @Test
    void 옵션_이름_실패_길이초과() {
        String longName = "a".repeat(51);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new OptionName(longName);
        });

        assertEquals(ErrorCode.INVALID_OPTION_NAME, exception.getErrorCode());
    }

    @Test
    void 옵션_이름_실패_유효하지않은문자포함() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new OptionName("옵션 이름!");
        });

        assertEquals(ErrorCode.INVALID_OPTION_NAME, exception.getErrorCode());
    }

    @Test
    void 옵션_이름_실패_공백만() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new OptionName("   ");
        });

        assertEquals(ErrorCode.INVALID_OPTION_NAME, exception.getErrorCode());
    }

    @Test
    void 옵션_이름_실패_특수문자포함() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new OptionName("옵션!이름");
        });

        assertEquals(ErrorCode.INVALID_OPTION_NAME, exception.getErrorCode());
    }
}
