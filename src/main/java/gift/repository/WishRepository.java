package gift.repository;

import gift.entity.WishEntity;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<WishEntity, Long> {

    Page<WishEntity> findByUserEntityId(Long userId, Pageable page);

    Optional<WishEntity> findByUserEntityIdAndProductEntityId(Long userId, Long ProductId);
}
