package gift.core.domain.user;

import java.util.Optional;

public interface UserAccountRepository {

    void save(Long userId, UserAccount userAccount);

    boolean exists(Long userId);

    boolean existsByPrincipal(String principal);

    Optional<UserAccount> findByUserId(Long userId);

    Optional<UserAccount> findByPrincipal(String principal);

    Long findUserIdByPrincipal(String principal);

    void delete(Long userId);

}
