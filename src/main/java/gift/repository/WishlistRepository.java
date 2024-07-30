package gift.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.entity.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
	
	Page<Wishlist> findByUserId(Long userId, Pageable pageable);
	Wishlist findByUserIdAndProductId(Long userId, Long productId);
}
