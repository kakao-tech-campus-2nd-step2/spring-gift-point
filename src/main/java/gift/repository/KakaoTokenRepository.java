package gift.repository;

import gift.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoTokenRepository extends JpaRepository<KakaoToken, String> {
    KakaoToken findByAccessToken(String accessToken);
}
