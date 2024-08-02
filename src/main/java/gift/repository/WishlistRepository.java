package gift.repository;

import gift.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Page<Wishlist> findByMemberId(Long memberId, Pageable pageable);

    Wishlist findByMemberIdAndProductId(Long memberId, Long productId);

}

