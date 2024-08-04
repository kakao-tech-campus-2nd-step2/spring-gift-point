package gift.doamin.wishlist.repository;

import gift.doamin.wishlist.entity.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishListRepository {

    Wish save(Wish wish);

    Page<Wish> findAllByUserId(Long userId, Pageable pageable);

    void update(Wish wish);

    Optional<Wish> findById(Long id);

    Optional<Wish> findByUserIdAndOptionId(Long userId, Long optionId);

    boolean existsByUserIdAndOptionId(Long userId, Long optionId);

    void delete(Wish wish);

    void deleteById(Long id);
}
