package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.users.user.User;
import gift.users.user.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    private User user;

    @BeforeEach
    void beforeEach(){
        user = new User("admin@email.com", "1234", "local");
    }

    @Test
    @DisplayName("회원 추가 테스트")
    void save() {
        //Given
        User expected = new User("admin@email.com", "1234", "local");

        //When
        User actual = userRepository.save(user);

        //Then
        assertThat(actual.getEmail()).isEqualTo(expected.getEmail());
        assertThat(actual.getPassword()).isEqualTo(expected.getPassword());
    }

    @Test
    @DisplayName("회원 아이디로 회원 찾기")
    void findById() {
        //Given
        userRepository.save(user);
        User expected = new User("admin@email.com", "1234", "local");

        //When
        Optional<User> actual = userRepository.findById(user.getId());

        //Then
        assertThat(actual).isPresent();
        assertThat(actual.get())
            .extracting(User::getEmail, User::getPassword)
            .containsExactly(expected.getEmail(), expected.getPassword());
    }

    @Test
    @DisplayName("회원 이메일로 회원 찾기")
    void findByEmail() {
        //Given
        userRepository.save(user);
        User expected = new User("admin@email.com", "1234", "local");

        //When
        User actual = userRepository.findByEmail(user.getEmail());

        //Then
        assertThat(actual)
            .extracting(User::getEmail, User::getPassword)
            .containsExactly(expected.getEmail(), expected.getPassword());
    }

    @Test
    @DisplayName("회원 이메일로 존재 여부 확인 시 존재함")
    void existsByEmail() {
        //Given
        userRepository.save(user);

        //When
        boolean actual = userRepository.existsByEmail(user.getEmail());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 회원을 찾으면 false 리턴")
    void notExistsByEmail() {
        //Given
        userRepository.save(user);

        //When
        boolean actual = userRepository.existsByEmail("example@email.com");

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("회원 이메일과 비밀번호로 찾기")
    void existsByEmailAndPassword() {
        //Given
        userRepository.save(user);

        //When
        boolean actual = userRepository.existsByEmailAndPassword(user.getEmail(),
            user.getPassword());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("이메일과 비밀번호가 둘 다 존재하지 않는 회원을 찾으면 false 리턴")
    void notExistsByEmailAndPassword() {
        //Given
        userRepository.save(user);

        //When
        boolean actual = userRepository.existsByEmailAndPassword("admin@email.com", "2222");

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("소셜 아이디와 소셜 이름으로 회원 존재 여부 확인 시 존재함")
    void existsBySnsIdAndSns(){
        //Given
        User kakaoUser = new User("123", "kakao");
        userRepository.save(kakaoUser);

        //When
        boolean actual = userRepository.existsBySnsIdAndSns(kakaoUser.getSnsId(), kakaoUser.getSns());

        //Then
        assertThat(actual).isEqualTo(true);
    }

    @Test
    @DisplayName("소셜 아이디와 소셜 이름으로 회원 존재 여부 확인 시 존재하지 않음")
    void notExistsBySnsIdAndSns(){
        //Given
        User kakaoUser = new User("123", "kakao");
        userRepository.save(kakaoUser);

        //When
        boolean actual = userRepository.existsBySnsIdAndSns("555", "kakao");

        //Then
        assertThat(actual).isEqualTo(false);
    }

    @Test
    @DisplayName("소셜 아이디와 소셜 이름으로 회원 찾기")
    void findBySnsIdAndSns(){
        //Given
        User kakaoUser = new User("123", "kakao");
        userRepository.save(kakaoUser);
        User expected = new User("123", "kakao");

        //When
        User actual = userRepository.findBySnsIdAndSns(kakaoUser.getSnsId(), kakaoUser.getSns());

        //Then
        assertThat(actual)
            .extracting(User::getSnsId, User::getSns)
            .containsExactly(expected.getSnsId(), expected.getSns());
    }
}
