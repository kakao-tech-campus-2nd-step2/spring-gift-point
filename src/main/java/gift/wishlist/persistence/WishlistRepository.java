package gift.wishlist.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface WishlistRepository extends JpaRepository<Wishlist,Long>,
    PagingAndSortingRepository<Wishlist, Long> {

    List<Wishlist> findByProductId(Long productId);

    List<Wishlist> findByMemberId(Long memberId);

    Page<Wishlist> findAllByMemberId(Long memberId, Pageable pageable);

    Page<Wishlist> findAll(Pageable pageable);
      
    boolean existsByProductIdAndMemberId(Long productId, Long memberId);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);

}
