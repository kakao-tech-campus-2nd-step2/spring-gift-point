package gift.repository;

import gift.domain.User;
import gift.service.CategoryService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void init(){
        User user1 = new User("one", "one@email.com", "1111");
        userRepository.save(user1);

        User user2 = new User("two", "two@email.com", "2222");
        userRepository.save(user2);

        User user3 = new User("three", "three@email.com", "3333");
        userRepository.save(user3);

        User user4 = new User("four", "four@email.com", "4444");
        userRepository.save(user4);

        User user5 = new User("five", "five@email.com", "5555");
        userRepository.save(user5);
    }

    @Test
    void findAllTest(){
        // when
        Pageable pageable = PageRequest.of(0, 5);
        Page<User> users = userRepository.findAll(pageable);
        long count = users.get().count();
        // then
        Assertions.assertThat(users).isNotNull();
        Assertions.assertThat(count).isEqualTo(5L);
    }

    @Test
    void findByIdTest(){
        // when
        User actual = userRepository.findById(1L).orElseThrow();
        User user1 = new User("one", "one@email.com", "1111");
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual.getUserId()).isEqualTo(user1.getUserId());
        Assertions.assertThat(actual.getEmail()).isEqualTo(user1.getEmail());
        Assertions.assertThat(actual.getPassword()).isEqualTo(user1.getPassword());
        Assertions.assertThat(actual.getAdmin()).isEqualTo(user1.getAdmin());
    }

    @Test
    void saveTest(){
        // when
        User user = new User("six", "six@email.com", "6666");
        User actual = userRepository.save(user);
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(actual).isEqualTo(user);
    }

    @Test
    void deleteTest(){
        // when
        User user = new User("six", "six@email.com", "6666");
        User actual = userRepository.save(user);
        userRepository.deleteById(6L);
        boolean tf = userRepository.existsById(6L);
        // then
        Assertions.assertThat(actual).isNotNull();
        Assertions.assertThat(tf).isEqualTo(false);
    }
}
