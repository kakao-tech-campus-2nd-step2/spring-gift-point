package gift.repository;

import gift.model.OauthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {

    boolean existsByMemberId(Long memberId);

    Optional<OauthToken> findByMemberId(Long memberId);

    void deleteByMemberId(Long memberId);
}
