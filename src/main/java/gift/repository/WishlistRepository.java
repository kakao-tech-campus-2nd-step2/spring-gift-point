package gift.repository;

import gift.model.Wishlist;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    List<Wishlist> findByUserUsername(String username);
    List<Wishlist> findByUserUsernameAndHiddenFalse(String username);
    Page<Wishlist> findByUserUsernameAndHiddenFalse(String username, Pageable pageable);
}
