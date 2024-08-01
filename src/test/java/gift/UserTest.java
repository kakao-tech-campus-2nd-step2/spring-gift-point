package gift;

import gift.domain.model.entity.User;
import gift.domain.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void saveUserTest() {
        // given
        User user = new User("test@example.com", "password123");

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("test@example.com");
        assertThat(savedUser.getPassword()).isEqualTo("password123");
    }

    @Test
    public void findByIdTest() {
        // given
        User user = new User("test@example.com", "password123");
        User savedUser = userRepository.save(user);

        // when
        User found = userRepository.findById(savedUser.getId()).orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void findByEmailTest() {
        // given
        User user = new User("test@example.com", "password123");
        userRepository.save(user);

        // when
        User found = userRepository.findByEmail("test@example.com").orElse(null);

        // then
        assertThat(found).isNotNull();
        assertThat(found.getEmail()).isEqualTo(user.getEmail());
        assertThat(found.getPassword()).isEqualTo(user.getPassword());
    }

    @Test
    public void updateUserTest() {
        // given
        User user = new User("test@example.com", "password123");
        User savedUser = userRepository.save(user);

        // when
        savedUser.update("updated@example.com", "newpassword123");
        User updatedUser = userRepository.save(savedUser);

        // then
        assertThat(updatedUser).isNotNull();
        assertThat(updatedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(updatedUser.getPassword()).isEqualTo("newpassword123");
    }

    @Test
    public void existsByEmailTest() {
        // given
        String email = "test@example.com";
        User user = new User(email, "password123");
        userRepository.save(user);

        // when
        boolean exists = userRepository.existsByEmail(email);
        boolean notExists = userRepository.existsByEmail("nonexistent@example.com");

        // then
        assertThat(exists).isTrue();
        assertThat(notExists).isFalse();
    }
}