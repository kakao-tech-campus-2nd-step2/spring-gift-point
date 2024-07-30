package gift.entity;

import gift.exception.BusinessException;
import gift.exception.ErrorCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void 유저_생성_성공() {
        User user = new User("test@example.com", "password123");

        assertNotNull(user);
        assertEquals("test@example.com", user.getEmail());
        assertEquals("password123", user.getPassword());
    }

    @Test
    void 유저_업데이트_성공() {
        User user = new User("test@example.com", "password123");

        user.update("new@example.com", "newpassword123");

        assertEquals("new@example.com", user.getEmail());
        assertEquals("newpassword123", user.getPassword());
    }

    @Test
    void 비밀번호_검증_성공() {
        User user = new User("test@example.com", "password123");

        assertTrue(user.isPasswordCorrect("password123"));
    }

    @Test
    void 비밀번호_검증_실패() {
        User user = new User("test@example.com", "password123");

        assertFalse(user.isPasswordCorrect("wrongpassword"));
    }

    @Test
    void 유저_업데이트_이메일_실패_유효하지않은형식() {
        User user = new User("test@example.com", "password123");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            user.update("invalidemail", "newpassword123");
        });

        assertEquals(ErrorCode.INVALID_EMAIL, exception.getErrorCode());
    }

    @Test
    void 유저_업데이트_비밀번호_실패_빈값() {
        User user = new User("test@example.com", "password123");

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            user.update("new@example.com", "");
        });

        assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
    }
}
