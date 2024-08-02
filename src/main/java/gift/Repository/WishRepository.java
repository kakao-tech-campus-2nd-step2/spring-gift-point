package gift.Repository;

import gift.Entity.WishEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {
    List<WishEntity> findByProductId(Long id);
    List<WishEntity> findByUserId(Long id);
    List<WishEntity> findByProductIdAndUserId(Long productId, Long userId);
}
