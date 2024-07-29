package gift.repository;

import gift.domain.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {

    void deleteByProductIdAndUserInfoId(Long productId, Long userId);

    Page<Wish> findByUserInfoId(Long userId, Pageable pageable);

    boolean existsByUserInfoIdAndProductId(Long userId, Long productId);

    Optional<Wish> findByUserInfoIdAndProductId(Long userId, Long productId);
}
