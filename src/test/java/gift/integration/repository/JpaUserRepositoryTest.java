package gift.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.User;
import gift.repository.JpaUserRepository;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class JpaUserRepositoryTest {
    @Autowired
    private JpaUserRepository jpaUserRepository;

    private User user;

    private Long insertUser(User user) {
        return jpaUserRepository.save(user).getId();
    }

    @BeforeEach
    void setUser() {
        user = new User("www.naver.com", "1234", "일반");
    }

    @Test
    void 회원_가입() {
        //given
        //when
        Long insertUserId = insertUser(user);
        User findUser = jpaUserRepository.findById(insertUserId).get();
        //then
        assertAll(
            () -> assertThat(findUser).isEqualTo(user)
        );
    }

    @Test
    void 회원_조회() {
        //given
        Long insertUserId = insertUser(user);
        //when
        User findUser = jpaUserRepository.findById(insertUserId).get();
        //then
        assertAll(
            () -> assertThat(findUser.getId()).isNotNull(),
            () -> assertThat(findUser.getId()).isEqualTo(insertUserId),
            () -> assertThat(findUser.getEmail()).isEqualTo("www.naver.com"),
            () -> assertThat(findUser.getPassword()).isEqualTo("1234"),
            () -> assertThat(findUser.getRole()).isEqualTo("일반"),

            () -> assertThrows(NoSuchElementException.class,
                () -> jpaUserRepository.findById(100L).get())
        );
    }

    @Test
    void 이메일_회원_조회() {
        //given
        Long insertUserId = insertUser(user);
        //when
        User findUserByEmail = jpaUserRepository.findByEmail("www.naver.com").get();
        //then
        assertAll(
            () -> assertThat(findUserByEmail.getId()).isNotNull(),
            () -> assertThat(findUserByEmail.getId()).isEqualTo(insertUserId),
            () -> assertThat(findUserByEmail.getEmail()).isEqualTo("www.naver.com"),
            () -> assertThat(findUserByEmail.getPassword()).isEqualTo("1234"),
            () -> assertThat(findUserByEmail.getRole()).isEqualTo("일반")
        );
    }
}