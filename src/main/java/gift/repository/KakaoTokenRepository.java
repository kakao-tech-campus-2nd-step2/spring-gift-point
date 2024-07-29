package gift.repository;

import gift.domain.KakaoToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, UUID> {
    Optional<KakaoToken> findByMemberId(UUID memberId);
}