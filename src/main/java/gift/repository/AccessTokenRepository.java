package gift.repository;

import gift.model.KakaoAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface AccessTokenRepository extends CrudRepository<KakaoAccessToken, Long> {
}
