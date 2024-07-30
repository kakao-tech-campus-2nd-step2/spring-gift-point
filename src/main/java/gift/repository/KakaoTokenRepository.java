package gift.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import gift.entity.KakaoToken;

import java.util.Optional;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, Long>{    
    Optional<KakaoToken> findByEmail(String email);
}
