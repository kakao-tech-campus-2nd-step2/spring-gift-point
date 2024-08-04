package gift.kakao.repository;

import gift.kakao.entity.KakaoToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoTokenRepository extends JpaRepository<KakaoToken, String> {

}
