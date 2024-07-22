package gift.auth.persistence;

import gift.auth.token.OAuthAccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OAuthAccessTokenRepository extends CrudRepository<OAuthAccessToken, String> {
}
