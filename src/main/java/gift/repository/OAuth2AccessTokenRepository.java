package gift.repository;

import gift.model.OAuth2AccessToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuth2AccessTokenRepository extends JpaRepository<OAuth2AccessToken, Long> {

    Optional<OAuth2AccessToken> findByMemberId(Long memberId);

    boolean existsByMemberId(Long memberId);
}
