package gift.repository;

<<<<<<< HEAD
import static org.assertj.core.api.Assertions.assertThat;
=======
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
>>>>>>> 524b151 (feat: 기본 코드 준비 (#98))
import gift.user.entity.User;
import gift.user.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserRepositoryTest {

  @Autowired
  private UserRepository userRepository;

  @BeforeEach
  public void setUp() {
    userRepository.deleteAll();
  }

  private User createAndSaveUser(String email, String password) {
    User user = new User();
    user.setEmail(email);
    user.setPassword(password);
    return userRepository.save(user);
  }

  @Test
  public void testFindByEmail() {
    // given
    User member = createAndSaveUser("test@example.com", "password");

    // when
    Optional<User> foundMember = userRepository.findByEmail("test@example.com");

    // then
    assertThat(foundMember).isPresent();
  }

  @Test
  public void testUpdateMember() {
    // given
    User member = createAndSaveUser("update@example.com", "initialPassword");

    // when
    member.setPassword("updatedPassword");
    userRepository.save(member);
    Optional<User> updatedMember = userRepository.findByEmail("update@example.com");

    // then
    assertThat(updatedMember).isPresent();
    updatedMember.ifPresent(m -> assertThat(m.getPassword()).isEqualTo("updatedPassword"));
  }

  @Test
  public void testDeleteMember() {
    // given
    User member = createAndSaveUser("delete@example.com", "password");

    // when
    userRepository.deleteByEmail("delete@example.com");
    Optional<User> deletedMember = userRepository.findByEmail("delete@example.com");

    // then
    assertThat(deletedMember).isNotPresent();
  }

  @Test
  public void testFindByNonExistingEmail() {
    // when
    Optional<User> foundMember = userRepository.findByEmail("nonexistent@example.com");

    // then
    assertThat(foundMember).isNotPresent();
  }
}
