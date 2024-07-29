package gift.repository;


import gift.user.domain.User;
import gift.user.repository.UserRepository;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByEmailPassword(){
        //given
        String email = "abc@naver.com";
        String password = "123";
        User expected = new User();
        expected.setEmail(email);
        expected.setPassword(password);
        userRepository.save(expected);
        //when
        Optional<User> actual = userRepository.findByEmailAndPassword(email, password);
        //then
        Assertions.assertThat(actual.get()).isEqualToComparingFieldByField(expected);
    }
}
