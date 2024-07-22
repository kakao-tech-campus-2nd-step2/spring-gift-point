package gift.user;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import gift.user.model.UserRepository;
import gift.user.model.dto.AppUser;
import gift.user.model.dto.Role;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppUserRepositoryTest {
    @Autowired
    private UserRepository userRepository;
    private AppUser appUser;

    @BeforeEach
    public void setUp() {
        appUser = new AppUser("aa@kakao.com", "1234", Role.USER, "aaaa");
        userRepository.save(appUser);
    }

    @Test
    public void testFindByEmailAndIsActiveTrue() {
        Optional<AppUser> foundUser = userRepository.findByEmail(appUser.getEmail());
        assertThat(foundUser).isPresent();
        assertThat(appUser).isEqualTo(foundUser.get());
    }

    @Test
    public void testFindByEmailAndIsActiveTrueIfUserFalse() {
        userRepository.delete(appUser);

        Optional<AppUser> foundUser = userRepository.findByEmail(appUser.getEmail());
        assertThat(foundUser).isEmpty();
    }

    @Test
    public void testFindByIdAndIsActiveTrue() {
        Optional<AppUser> foundUser = userRepository.findById(appUser.getId());
        assertThat(foundUser).isPresent();
        assertThat(appUser).isEqualTo(foundUser.get());
    }

    @Test
    public void testUpdatePassword() {
        appUser.updatePassword("1111");
        userRepository.save(appUser);
        Optional<AppUser> updatedUser = userRepository.findById(appUser.getId());
        assertThat(updatedUser.get().getPassword()).isEqualTo("1111");
    }

    @Test
    public void testUniqueEmailConstraint() {
        assertThatThrownBy(() ->
                userRepository.save(new AppUser("aa@kakao.com", "1234", Role.USER, "aaaa"))
        ).isInstanceOf(DataIntegrityViolationException.class);
    }
}
