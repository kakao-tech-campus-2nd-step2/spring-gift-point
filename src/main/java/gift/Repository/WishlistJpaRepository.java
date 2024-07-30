package gift.Repository;

import gift.Entity.Member;
import gift.Entity.Wishlist;
import gift.Entity.WishlistId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface WishlistJpaRepository extends JpaRepository<Wishlist, WishlistId> {

    List<Wishlist> findByIdMemberId(long memberId);

    @Query("SELECT w FROM Wishlist w WHERE w.id.memberId = :memberId AND w.id.productId = :productId")
    Optional<Wishlist> findByWishlistId(@Param("memberId") long memberId, @Param("productId") long productId);

    Page<Wishlist> findByMember(Member member, Pageable pageable);

    void deleteByIdMemberId(long memberId);

}
