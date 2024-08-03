package gift.repository.auth;

import gift.entity.auth.SocialEntity;
import gift.entity.enums.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SocialRepository extends JpaRepository<SocialEntity, Long> {

    Optional<SocialEntity> findBySocialIdAndType(Long socialId, SocialType socialType);
}
