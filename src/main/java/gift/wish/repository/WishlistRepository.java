package gift.wish.repository;

import gift.wish.domain.WishlistItem;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public
interface WishlistRepository extends JpaRepository<WishlistItem, Long> {

    Page<WishlistItem> findListByUserId(Long userId, Pageable pageable);
    List<WishlistItem> findListByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM WishlistItem w WHERE w.user.id = ?1 AND w.product.id = ?2")
    void deleteByUserIdProductId(Long userId, Long productId);
}
