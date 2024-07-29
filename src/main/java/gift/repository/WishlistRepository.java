package gift.repository;

import gift.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findBySiteUserUsernameAndHiddenFalseOrderByCreatedDateDesc(String username, Pageable pageable);
}
