package gift.product.repository;

import gift.product.model.KakaoToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long> {

    Optional<KakaoToken> findByMemberId(Long memberId);
}
