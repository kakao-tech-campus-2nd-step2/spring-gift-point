package gift.repository;

import gift.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        user1 = userRepository.save(new User("user1@test.com", "password1", "User One"));
        user2 = userRepository.save(new User("user2@test.com", "password2", "User Two"));
    }

    @Test
    void save() {
        User expected = new User("aaa@aaa", "aaa", "user");

        User actual = userRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
                () -> assertThat(actual.getPassword()).isEqualTo(expected.getPassword()),
                () -> assertThat(actual.getRole()).isEqualTo(expected.getRole())
        );
    }

    @Test
    void findByEmail() {
        User actual = userRepository.findByEmail(user1.getEmail()).orElse(null);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(user1.getEmail()),
                () -> assertThat(actual.getPassword()).isEqualTo(user1.getPassword()),
                () -> assertThat(actual.getRole()).isEqualTo(user1.getRole())
        );
    }

    @Test
    void existsByEmail() {
        boolean exists = userRepository.existsByEmail(user1.getEmail());
        assertThat(exists).isTrue();

        boolean notExist = userRepository.existsByEmail("bbb@bbb");
        assertThat(notExist).isFalse();
    }
}