package gift.domain.wishlist.repository;

import gift.domain.product.entity.Product;
import gift.domain.member.entity.Member;
import gift.domain.wishlist.entity.WishItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistJpaRepository extends JpaRepository<WishItem, Long> {

    @Query("select w from WishItem w join fetch w.member m join fetch w.product p where m.id = :memberId")
    Page<WishItem> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query("delete from WishItem w where w.member.id = :memberId")
    @Modifying
    void deleteAllByMemberId(@Param("memberId") Long memberId);

    @Query("delete from WishItem w where w.product.id = :productId")
    @Modifying
    void deleteAllByProductId(@Param("productId") Long productId);

    void deleteByMemberAndProduct(Member member, Product product);
}
