package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    private final String testEmail = "test@test";
    private final String testPassword = "testPw";
    private final User testUser = new User(testEmail, testPassword);

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("유저 추가 성공 테스트")
    void testSaveSuccess(){
        User saved = userRepository.save(testUser);
        assertThat(saved).isNotNull();
    }

    @Test
    @DisplayName("유저 조회 성공 테스트")
    void testFindUserSuccessTest(){
        userRepository.save(testUser);
        User result = userRepository.findByEmail(testEmail).orElse(null);
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("유저 조회 실패 테스트")
    void testFindUserFailTest(){
        User result = userRepository.findByEmail(testEmail).orElse(null);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("이메일 존재 여부 테스트")
    void testExistsEmail(){
        String testEmail1 = "test@test";
        String testEmail2 = "asdf@asdf";
        userRepository.save(testUser);
        boolean result1 = userRepository.existsByEmail(testEmail1);
        boolean result2 = userRepository.existsByEmail(testEmail2);
        assertThat(result1).isTrue();
        assertThat(result2).isFalse();
    }
}
