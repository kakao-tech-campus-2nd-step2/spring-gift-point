package gift.repository.token;

import gift.domain.AuthToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<AuthToken, Long> {

    Optional<AuthToken> findAuthTokenByToken(String token);

    Optional<AuthToken> findTokenByEmail(String email);

}
