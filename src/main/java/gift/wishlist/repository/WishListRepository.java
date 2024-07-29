package gift.wishlist.repository;

import gift.wishlist.entity.WishList;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Page<WishList> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<WishList> findByIdAndMemberId(Long id, Long memberId);

    Optional<WishList> findByMemberIdAndProductId(Long memberId, Long productId);

    boolean existsByMemberIdAndProductId(Long memberId, Long productId);
}
