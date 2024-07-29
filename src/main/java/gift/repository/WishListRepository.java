package gift.repository;

import gift.domain.WishProduct;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WishListRepository extends JpaRepository<WishProduct, Long> {
    WishProduct save(WishProduct wishProduct);

    List<WishProduct> findAll();

    List<WishProduct> findByUserId(Long userId);

    Page<WishProduct> findByUserId(Long userId, Pageable pageable);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

    WishProduct findByUserIdAndProductId(Long userId, Long productId);
}
