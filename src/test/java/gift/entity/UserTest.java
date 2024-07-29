package gift.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("test@example.com", "password123");
    }

    @Test
    @DisplayName("생성자 테스트")
    void UserConstructorTest() {
        User newUser = new User("new@example.com", "newpassword");

        assertThat(newUser).isNotNull();
        assertThat(newUser.getEmail()).isEqualTo("new@example.com");
        assertThat(newUser.getPassword()).isEqualTo("newpassword");
    }

    @Test
    @DisplayName("Getter 테스트")
    void UserGetterTest() {
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
    }

    @Test
    @DisplayName("samePassword 테스트")
    void UserSamePasswordTest() {
        assertThat(user.samePassword("password123")).isTrue();
        assertThat(user.samePassword("wrongpassword")).isFalse();
    }
}
