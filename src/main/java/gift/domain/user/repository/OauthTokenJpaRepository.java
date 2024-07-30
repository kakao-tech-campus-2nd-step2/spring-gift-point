package gift.domain.user.repository;

import gift.domain.user.entity.AuthProvider;
import gift.domain.user.entity.OauthToken;
import gift.domain.user.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OauthTokenJpaRepository extends JpaRepository<OauthToken, Long> {

    Optional<OauthToken> findByUserAndProvider(User user, AuthProvider provider);
}
