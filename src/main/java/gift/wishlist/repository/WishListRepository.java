package gift.wishlist.repository;

import gift.product.entity.Product;
import gift.wishlist.entity.WishList;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    boolean existsByUserIdAndProduct(long userId, Product product);

    Optional<WishList> findByUserIdAndProduct(long userId, Product product);

    Page<WishList> findByUserId(long userId, Pageable pageable);
}
