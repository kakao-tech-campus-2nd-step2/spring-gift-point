package gift.repository;

import gift.model.OauthToken;
import gift.model.OauthType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {

    boolean existsByMemberId(Long memberId);

    Optional<OauthToken> findByMemberIdAndOauthType(Long memberId, OauthType oauthType);

    void deleteByMemberId(Long memberId);
}
