package gift.product.repository;

import gift.product.model.SnsMember;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SnsMemberRepository extends JpaRepository<SnsMember, Long> {
    boolean existsByKakaoId(Long kakaoId);
    Optional<SnsMember> findByKakaoId(Long kakaoId);
}
