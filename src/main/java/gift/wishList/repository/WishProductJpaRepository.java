package gift.wishList.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import gift.wishList.domain.WishProduct;

@Repository
public interface WishProductJpaRepository extends JpaRepository<WishProduct, Long> {
	List<WishProduct> findByUserId(Long userId);
	Page<WishProduct> findByUserId(Long userId, Pageable pageable);
}
