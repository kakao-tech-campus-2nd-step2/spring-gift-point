package gift.repository.token;

import gift.domain.Member;
import gift.domain.TokenAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenSpringDataJpaRepository extends JpaRepository<TokenAuth, Long> {
    Optional<TokenAuth> findByAccessToken(String accessToken);

    Optional<TokenAuth> findByMember(Member member);

    Optional<TokenAuth> findByRefreshToken(String refreshToken);
}