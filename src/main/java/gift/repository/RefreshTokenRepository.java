package gift.repository;

import gift.model.KakaoRefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<KakaoRefreshToken, Long> {
}
