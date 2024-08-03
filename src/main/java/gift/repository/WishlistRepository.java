package gift.repository;

import gift.model.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long> {
    Page<Wishlist> findByUserEmail(String email, Pageable pageable);
    void deleteByUserEmailAndProductId(String email, Long productId);
    void deleteByProductId(Long productId);
}