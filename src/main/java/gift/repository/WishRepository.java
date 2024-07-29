package gift.repository;

import gift.model.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishRepository extends JpaRepository<Wish, Long> {

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    @Query("SELECT w FROM Wish w JOIN FETCH w.product WHERE w.user.id = :userId")
    Page<Wish> findByUserId(Long userId, Pageable pageable);

    void deleteByProductIdAndUserId(Long productId, Long userId);

    Optional<Wish> findByProductIdAndUserId(Long productId, Long userId);

    void deleteByProductId(Long productId);
}
