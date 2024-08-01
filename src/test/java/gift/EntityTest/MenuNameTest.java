package gift.EntityTest;

import gift.domain.Menu.MenuName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class MenuNameTest {
    @Test
    @DisplayName("유효한 이름 테스트")
    public void testValidMenuName() {
        MenuName menuName = new MenuName("ValidName");

        assertDoesNotThrow(() -> menuName.validMenuName("ValidName123"));
        assertDoesNotThrow(() -> menuName.validMenuName("유효한이름"));
        assertDoesNotThrow(() -> menuName.validMenuName("Valid Name"));
    }

    @Test
    @DisplayName("길이가 15자 이상인 경우 테스트")
    public void testMenuNameTooLong() {
        MenuName menuName = new MenuName();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            menuName.validMenuName("1234567890_123456"); // 16 characters
        });
        assertEquals("메뉴는 15자 이내로 작성해주세요", thrown.getMessage());
    }

    @Test
    @DisplayName("특수기호인 경우 테스트")
    public void testMenuNameContainsInvalidCharacters() {
        MenuName menuName = new MenuName();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            menuName.validMenuName("Invalid@Name");
        });
        assertEquals(" ( ), [ ], +, -, &, /, _ 이외의 특수문자는 사용이 불가능합니다.", thrown.getMessage());
    }

    @Test
    @DisplayName("카카오인 경우 테스트")
    public void testMenuNameContainsKakao() {
        MenuName menuName = new MenuName();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            menuName.validMenuName("카카오톡");
        });
        assertEquals("'카카오'가 들어간 상품명은 담당 MD와 상의해주세요", thrown.getMessage());
    }
}
