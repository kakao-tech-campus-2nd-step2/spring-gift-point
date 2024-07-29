package gift.repository;

import gift.domain.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {
    Optional<KakaoToken> findByMemberId(Long memberId);
}
