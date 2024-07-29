package gift.order.repository;

import gift.order.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenJPARepository extends JpaRepository<Token, Long> {
    Token findByAccessToken(String accessToken);

    Token findByRefreshToken(String refreshToken);
}
