package gift.main.repository;

import gift.main.entity.WishProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishProductRepository extends JpaRepository<WishProduct, Long> {
    void deleteByProductIdAndUserId(Long productId, Long userId);

    boolean existsAllByUserId(Long userId);

    boolean existsByProductIdAndUserId(Long productId, Long userId);

    Optional<WishProduct> findByProductIdAndUserId(Long productId, Long userId);

    Page<WishProduct> findAllByUserId(Long userId, Pageable pageable);

    List<WishProduct> findAllByProductId(Long productId);

}
