package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.User;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        User expected = new User("test@abc.com", "password");
        User actual = userRepository.save(expected);

        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
    }

    @Test
    @DisplayName("이메일 기반으로 유저 찾기 테스트")
    void findByEmailTest() {
        User user = new User("test@abc.com", "password");
        User actual = userRepository.save(user);
        Optional<User> expected = userRepository.findByEmail(user.getEmail());

        assertThat(actual.getId()).isNotNull();
        assertThat(expected).isPresent();
        assertThat(actual.samePassword(expected.get().getPassword())).isTrue();
    }

    @Test
    @DisplayName("저장되어있지 않은 이메일로 유저를 찾을 때 빈 Optional을 리턴하는지 테스트")
    void edgeCaseTest() {
        String NotSavedEmail = "notSavedEmail@abc.com";

        assertThat(userRepository.findByEmail(NotSavedEmail)).isEmpty();
    }
}
