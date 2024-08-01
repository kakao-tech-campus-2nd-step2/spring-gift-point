package gift.Repository;

import gift.DTO.KakaoJwtToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KakaoJwtTokenRepository extends JpaRepository<KakaoJwtToken, Long> {

}
