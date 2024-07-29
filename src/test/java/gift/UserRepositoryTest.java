package gift;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.User;
import gift.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void saveUser_ReturnSavedUser() {
        // given
        User expected = new User(null, "test@example.com", "password");

        // when
        User actual = userRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail())
        );
    }

    @Test
    void findUserByEmail_ReturnUser_WhenUserExists() {
        // given
        String expectedEmail = "test@example.com";
        User expected = new User(null, expectedEmail, "password");
        userRepository.save(expected);

        // when
        User actual = userRepository.findByEmail(expectedEmail);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(expectedEmail);
    }
}