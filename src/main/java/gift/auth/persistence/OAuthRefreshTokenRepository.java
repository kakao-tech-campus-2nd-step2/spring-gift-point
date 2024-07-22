package gift.auth.persistence;

import gift.auth.token.OAuthRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface OAuthRefreshTokenRepository extends CrudRepository<OAuthRefreshToken, String> {
}
