package gift.doamin.user.repository;

import gift.doamin.user.entity.KakaoOAuthToken;
import gift.doamin.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoOAuthTokenRepository extends JpaRepository<KakaoOAuthToken, Long> {

    Optional<KakaoOAuthToken> findByUser(User user);

}
