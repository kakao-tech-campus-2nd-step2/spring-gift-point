package gift.doamin.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.doamin.user.entity.User;
import gift.doamin.user.entity.UserRole;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class JpaUserRepositoryTest {

    @Autowired
    JpaUserRepository jpaUserRepository;

    @Test
    void save() {
        User user = new User("test@test.com", "1234", "test", UserRole.USER);

        User savedUser = jpaUserRepository.save(user);

        assertThat(savedUser.getId()).isNotNull();
    }

    @Test
    void save_duplicatedEmail() {
        User user1 = new User("test@test.com", "1234", "test1", UserRole.USER);
        jpaUserRepository.save(user1);
        User user2 = new User("test@test.com", "1234", "test2", UserRole.USER);

        assertThatThrownBy(() -> jpaUserRepository.save(user2));
    }

    @Test
    void existsByEmail() {
        User user = new User("test1@test.com", "1234", "test", UserRole.USER);
        jpaUserRepository.save(user);

        assertAll(
            () -> assertThat(jpaUserRepository.existsByEmail("test1@test.com")).isTrue(),
            () -> assertThat(jpaUserRepository.existsByEmail("test2@test.com")).isFalse()
        );
    }

    @Test
    void findByEmail() {
        User user = new User("test1@test.com", "1234", "test", UserRole.USER);
        User savedUser = jpaUserRepository.save(user);

        Optional<User> foundUser = jpaUserRepository.findByEmail("test1@test.com");

        assertThat(foundUser.get()).isEqualTo(savedUser);
    }

}