package gift.repository;

import gift.model.oauth.KakaoToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<KakaoToken, Long> {

    Optional<KakaoToken> findByMemberId(Long memberId);
}
