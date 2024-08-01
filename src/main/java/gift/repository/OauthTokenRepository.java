package gift.repository;

import gift.model.OauthToken;
import gift.model.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {

    boolean existsByMemberIdAndOauthType(Long memberId, OauthType oauthType);

    Optional<OauthToken> findByMemberIdAndOauthType(Long memberId, OauthType oauthType);

    void deleteAllByMemberId(Long memberId);
}
