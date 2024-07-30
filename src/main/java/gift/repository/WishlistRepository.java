package gift.repository;

import gift.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findByMemberId(Long memberId, Pageable pageable);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);

    void deleteByProductId(Long productId);
}
