package gift.repository.token;

import gift.domain.Member;
import gift.domain.TokenAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.Optional;

public interface TokenSpringDataJpaRepository extends JpaRepository<TokenAuth, Long> {
    Optional<TokenAuth> findByAccessToken(String accessToken);

    Optional<TokenAuth> findByMember(Member member);

    Optional<TokenAuth> findByRefreshToken(String refreshToken);

    @Query("DELETE FROM TokenAuth t WHERE t.accessTokenExpiry < :now OR t.refreshTokenExpiry < :now")
    void deleteAllExpiredSince(Date now);
}