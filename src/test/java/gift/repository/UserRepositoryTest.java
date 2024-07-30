package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import gift.model.user.User;
import gift.repository.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @DisplayName("회원 정보 저장 테스트")
    @Test
    void save() {
        // given
        User user = createUser("yj", "yj@google.com", "password", "BE");
        // when
        User savedUser = userRepository.save(user);
        // then
        Assertions.assertAll(
            () -> assertThat(savedUser.getId()).isNotNull(),
            () -> assertThat(savedUser.getEmail()).isEqualTo(user.getEmail())
        );
    }

    @DisplayName("id에 따른 회원 찾기 테스트")
    @Test
    void findById() {
        // given
        User user = createUser("yj", "yj@google.com", "password", "BE");
        userRepository.save(user);
        Long id = user.getId();

        // when
        Optional<User> findUser = userRepository.findById(id);
        // then
        assertThat(findUser).isNotNull();
    }

    private User createUser(String name, String email, String password, String Role) {
        return new User(name, email, password, Role);
    }

}
