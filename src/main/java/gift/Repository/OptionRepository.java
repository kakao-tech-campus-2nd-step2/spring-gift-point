package gift.Repository;

import gift.Model.Entity.OptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionRepository extends JpaRepository<OptionEntity, Long> {
    Optional<OptionEntity> findByName(String name);
    List<OptionEntity> findByProductId(Long productId);
}
