package gift.repository;

import gift.dto.user.UserRequestDTO;
import gift.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    private String testEmail = "test@naver.com";
    private UserRequestDTO user;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        user = new UserRequestDTO(testEmail, "123");
        userRepository.save(new User(user));
    }

    @Test
    void existsByEmail_존재O() {
        // given
        // when
        boolean expect = userRepository.existsByEmail(testEmail);

        // then
        assertThat(expect).isTrue();
    }

    @Test
    void existsByEmail_존재X() {
        // given
        // when
        boolean expect = userRepository.existsByEmail("없는@이메일.com");

        // then
        assertThat(expect).isFalse();
    }
}
