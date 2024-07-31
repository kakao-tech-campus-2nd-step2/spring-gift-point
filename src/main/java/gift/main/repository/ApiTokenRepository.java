package gift.main.repository;

import gift.main.entity.ApiToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ApiTokenRepository extends JpaRepository<ApiToken, Long> {
    Optional<ApiToken> findByUserId(Long userId);

    boolean existsByUserId(long userId);
}
