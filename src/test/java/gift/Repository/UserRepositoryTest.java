package gift.Repository;

import gift.Entity.UserEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        UserEntity user = new UserEntity("test@example.com", "password123");

        UserEntity savedUser = userRepository.save(user);

        assertUser(savedUser, "test@example.com", "password123");
    }

    @Test
    public void testFindByEmail() {
        UserEntity user = new UserEntity("unique@example.com", "password456");
        userRepository.save(user);

        Optional<UserEntity> foundUser = userRepository.findByEmail("unique@example.com");

        assertThat(foundUser).isPresent();
        assertUser(foundUser.get(), "unique@example.com", "password456");
    }

    private void assertUser(UserEntity user, String expectedEmail, String expectedPassword) {
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo(expectedEmail);
        assertThat(user.getPassword()).isEqualTo(expectedPassword);
    }
}
