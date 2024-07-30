package gift.member.oauth;

import gift.member.oauth.model.OauthToken;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OauthTokenRepository extends JpaRepository<OauthToken, Long> {

    OauthToken findByProviderAndEmail(String provider, String email);

    boolean existsByProviderAndEmail(String provider, String email);

    Optional<OauthToken> findByMemberId(Long memberId);

    Optional<OauthToken> findByAccessToken(String accessToken);
}
