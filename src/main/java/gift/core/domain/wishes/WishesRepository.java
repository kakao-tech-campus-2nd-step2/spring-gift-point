package gift.core.domain.wishes;

import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.user.User;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishesRepository {

    void saveWish(Long userId, Long productId);

    void removeWish(Long userId, Long productId);

    boolean exists(Long userId, Long productId);

    List<Product> getWishlistOfUser(User user);

    PagedDto<Product> getWishlistOfUser(User user, Pageable pageable);

}
