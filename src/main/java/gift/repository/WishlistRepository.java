package gift.repository;

import gift.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findByMemberId(Long memberId, Pageable pageable);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);

    void deleteByProductId(Long productId);

    @Query("SELECT w.product.id FROM Wishlist w WHERE w.id = :wishlistId")
    Optional<Long> findProductIdByWishlistId(@Param("wishlistId") Long wishlistId);
}
