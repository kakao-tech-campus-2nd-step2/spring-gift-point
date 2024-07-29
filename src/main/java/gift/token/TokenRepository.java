package gift.token;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByUserIdAndSns(long userId, String sns);

    boolean existsByUserIdAndSns(long userId, String sns);
}
