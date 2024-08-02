package gift.domain.member.repository;

import gift.domain.member.entity.AuthProvider;
import gift.domain.member.entity.Member;
import gift.domain.member.entity.OauthToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokenJpaRepository extends JpaRepository<OauthToken, Long> {

    Optional<OauthToken> findByMemberAndProvider(Member member, AuthProvider provider);
}
