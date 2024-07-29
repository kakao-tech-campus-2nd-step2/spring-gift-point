package gift.user.infrastructure.persistence.repository;

import gift.core.domain.user.UserAccount;
import gift.core.domain.user.UserAccountRepository;
import gift.user.infrastructure.persistence.entity.UserAccountEntity;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserAccountRepositoryImpl implements UserAccountRepository {
    private final JpaUserAccountRepository jpaUserAccountRepository;

    public UserAccountRepositoryImpl(JpaUserAccountRepository jpaUserAccountRepository) {
        this.jpaUserAccountRepository = jpaUserAccountRepository;
    }

    @Override
    public void save(Long userId, UserAccount userAccount) {
        jpaUserAccountRepository.save(UserAccountEntity.of(userId, userAccount));
    }

    @Override
    public boolean exists(Long userId) {
        return jpaUserAccountRepository.existsById(userId);
    }

    @Override
    public boolean existsByPrincipal(String principal) {
        return jpaUserAccountRepository.existsByEmail(principal);
    }

    @Override
    public Optional<UserAccount> findByUserId(Long userId) {
        return jpaUserAccountRepository.findById(userId)
                .map(this::mapToUserAccount);
    }

    @Override
    public Optional<UserAccount> findByPrincipal(String principal) {
        return jpaUserAccountRepository.findByEmail(principal)
                .map(this::mapToUserAccount);
    }

    @Override
    public Long findUserIdByPrincipal(String principal) {
        return jpaUserAccountRepository.findByEmail(principal)
                .map(UserAccountEntity::getUserId)
                .orElse(null);
    }

    @Override
    public void delete(Long userId) {
        jpaUserAccountRepository.deleteById(userId);
    }

    private UserAccount mapToUserAccount(UserAccountEntity entity) {
        return new UserAccount(entity.getEmail(), entity.getPassword());
    }
}
