package gift.doamin.wishlist.repository;

import gift.doamin.wishlist.entity.Wish;
import java.util.List;

public interface WishListRepository {

    Wish save(Wish wish);

    List<Wish> findByUserId(Long userId);

    Wish findByUserIdAndProductId(Long userId, Long productId);

    void update(Wish wish);

    void deleteById(Long id);

    boolean existsByUserIdAndProductId(Long userId, Long productId);

}
