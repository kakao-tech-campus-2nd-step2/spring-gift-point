package gift.repository.wish;

import gift.model.product.Product;
import gift.model.user.User;
import gift.model.wishlist.WishList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {
    boolean existsByUserAndProduct(User user, Product product);
    Page<WishList> findAllByUser(User user, Pageable pageable);
    Optional<WishList> findByIdAndUserId(Long wishId, Long userId);
    void deleteByUserIdAndAndProductId(Long userId, Long productId);
}

