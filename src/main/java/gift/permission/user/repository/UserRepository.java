package gift.permission.user.repository;

import gift.permission.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByPlatformUniqueId(String platformUniqueId);

    Optional<User> findByRefreshToken(String refreshToken);
}
