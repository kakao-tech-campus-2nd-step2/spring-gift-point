package gift.repository;

import gift.domain.Wishlist;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {

    Optional<Wishlist> findByMemberEmailAndProductId(String email, Long productId);

    Page<Wishlist> findByMemberEmail(String email, Pageable pageable);
}
