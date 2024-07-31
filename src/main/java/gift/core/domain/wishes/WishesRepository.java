package gift.core.domain.wishes;

import gift.core.PagedDto;
import gift.core.domain.product.Product;
import gift.core.domain.user.User;
import gift.wishes.service.WishDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface WishesRepository {

    void saveWish(Long userId, Long productId);

    void removeWish(Long userId, Long productId);

    boolean exists(Long userId, Long productId);

    List<WishDto> getWishlistOfUser(User user);

    PagedDto<WishDto> getWishlistOfUser(User user, Pageable pageable);

}
