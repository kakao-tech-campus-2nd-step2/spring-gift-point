package gift.repository.wishlist;

import gift.domain.WishlistItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistSpringDataJpaRepository extends JpaRepository<WishlistItem, Long> {
    List<WishlistItem> findByMemberId(Long memberId);

    Page<WishlistItem> findByMemberId(Long memberId, Pageable pageable);

    Optional<WishlistItem> findByMemberIdAndProductId(Long MemberId, Long productId);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}