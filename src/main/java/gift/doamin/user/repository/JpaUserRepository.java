package gift.doamin.user.repository;

import gift.doamin.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
