package gift.user.infrastructure.persistence.repository;

import gift.core.domain.user.User;
import gift.core.domain.user.UserAccount;
import gift.core.domain.user.UserAccountRepository;
import gift.core.domain.user.UserRepository;
import gift.core.domain.user.exception.UserAccountNotFoundException;
import gift.user.infrastructure.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private final JpaUserRepository jpaUserRepository;
    private final UserAccountRepository userAccountRepository;

    @Autowired
    public UserRepositoryImpl(
            JpaUserRepository jpaUserRepository,
            UserAccountRepository userAccountRepository
    ) {
        this.jpaUserRepository = jpaUserRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @Override
    public void save(User user) {
        UserEntity savedUser = jpaUserRepository.save(UserEntity.fromDomain(user));
        userAccountRepository.save(savedUser.getId(), user.account());
    }

    @Override
    public boolean existsById(Long id) {
        return jpaUserRepository.existsById(id);
    }

    @Override
    public Optional<User> findById(Long id) {
        return jpaUserRepository.findById(id)
                .map(this::mapToUser);
    }

    @Override
    public void deleteById(Long id) {
        jpaUserRepository.deleteById(id);
    }

    private User mapToUser(UserEntity entity) {
        UserAccount userAccount = userAccountRepository
                .findByUserId(entity.getId())
                .orElseThrow(UserAccountNotFoundException::new);
        return new User(
                entity.getId(),
                entity.getName(),
                userAccount
        );
    }
}
