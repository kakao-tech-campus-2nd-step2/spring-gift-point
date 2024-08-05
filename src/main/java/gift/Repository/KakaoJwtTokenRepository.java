package gift.Repository;

import gift.DTO.KakaoJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KakaoJwtTokenRepository extends JpaRepository<KakaoJwtToken, Long> {

}
