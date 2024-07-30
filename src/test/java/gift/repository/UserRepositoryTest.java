package gift.repository;

import gift.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void 사용자_저장_조회_성공() {
        User user = new User("test@example.com", "password");
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("test@example.com", foundUser.get().getEmail());
    }

    @Test
    public void 사용자_삭제_성공() {
        User user = new User("test@example.com", "password");
        userRepository.save(user);

        userRepository.delete(user);

        Optional<User> foundUser = userRepository.findByEmail("test@example.com");
        assertFalse(foundUser.isPresent());
    }
}
