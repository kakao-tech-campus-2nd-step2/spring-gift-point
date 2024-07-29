package gift.user;

import gift.core.domain.user.UserAccount;
import gift.user.infrastructure.persistence.repository.JpaUserAccountRepository;
import gift.user.infrastructure.persistence.entity.UserAccountEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserAccountRepositoryTests {
    @Autowired
    private JpaUserAccountRepository jpaUserAccountRepository;

    @Test
    public void saveUserAccount() {
        UserAccountEntity userAccount = UserAccountEntity.of(0L, sampleUserAccount());

        userAccount = jpaUserAccountRepository.save(userAccount);

        assertThat(jpaUserAccountRepository.findById(userAccount.getUserId())).isPresent();
        assertThat(jpaUserAccountRepository.findById(userAccount.getUserId()).get()).isEqualTo(userAccount);
    }

    @Test
    public void findByEmail() {
        UserAccountEntity userAccount = UserAccountEntity.of(0L, sampleUserAccount());
        jpaUserAccountRepository.save(userAccount);

        assertThat(jpaUserAccountRepository.findByEmail("test")).isPresent();
    }

    @Test
    public void existsByEmail() {
        UserAccountEntity userAccount = UserAccountEntity.of(0L, sampleUserAccount());
        jpaUserAccountRepository.save(userAccount);

        assertThat(jpaUserAccountRepository.existsByEmail("test")).isTrue();
    }

    private UserAccount sampleUserAccount() {
        return new UserAccount("test", "test");
    }
}
