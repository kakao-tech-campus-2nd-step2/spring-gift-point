package gift.repository;

import gift.domain.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token,Long> {

    Optional<Token> findByEmail(String email);
}
