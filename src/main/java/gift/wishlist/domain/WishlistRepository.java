package gift.wishlist.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Page<Wishlist> findAllByMemberId(Long memberId, Pageable pageable);

    List<Wishlist> findAllByMemberId(Long memberId);

    Page<Wishlist> findAllByProductId(Long productId, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.member.id = :memberId")
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Modifying
    @Query("DELETE FROM Wishlist w WHERE w.product.id = :productId")
    void deleteAllByProductId(@Param("productId") Long productId);
}
