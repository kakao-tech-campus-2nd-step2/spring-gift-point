package gift.doamin.user.repository;

import gift.doamin.user.entity.RefreshToken;
import gift.doamin.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByUser(User user);

    Optional<RefreshToken> findByToken(String refreshToken);
}
