package gift.Repository;

import gift.Entity.KakaoUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoUserRepository extends JpaRepository<KakaoUserEntity, Long> {
    KakaoUserEntity findByKakaoId(String kakaoId);
    KakaoUserEntity findByAccessToken(String accessToken);
}
