package gift.repository;

import gift.common.enums.LoginType;
import gift.model.user.User;
import gift.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        User user1 = new User("test1@example.com", "password1", "testName1", LoginType.DEFAULT);
        User user2 = new User("test2@example.com", "password2", "testName2", LoginType.DEFAULT);
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @DisplayName("유저 정보가 잘 저장되는지 테스트")
    void testSave() {
        User user = new User("abc@email.com", "1234", "testName1", LoginType.DEFAULT);
        User actual = userRepository.save(user);

        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getEmail()).isEqualTo(user.getEmail())
        );
    }

    @Test
    @DisplayName("이메일로 유저찾기가 잘 실행되는지 테스트")
    void testFindByEmail() {
        // given
        String emailToFind = "test1@example.com";

        // when
        Optional<User> foundUser = userRepository.findByEmail(emailToFind);

        // then
        assertAll(
                () -> assertThat(foundUser).isPresent(),
                () -> assertThat(foundUser.get().getEmail()).isEqualTo(emailToFind)
        );
    }

}