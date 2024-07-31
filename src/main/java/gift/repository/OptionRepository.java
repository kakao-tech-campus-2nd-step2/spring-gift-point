package gift.repository;

import gift.entity.OptionEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {
    List<OptionEntity> findByProductId(Long productId);
}
