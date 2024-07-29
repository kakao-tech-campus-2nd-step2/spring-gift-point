package gift.Repository;

import gift.Model.Entity.WishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {
    List<WishEntity> findByMemberId(Long memberId);
    WishEntity findByMemberIdAndProductId(Long memberId, Long productId);
}
