package gift.users.wishlist;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishListRepository extends JpaRepository<WishList, Long> {

    Page<WishList> findAllByUserId(long id, Pageable pageable);

    void deleteByUserIdAndProductIdAndOptionId(long userId, long productId, long optionId);

    boolean existsByIdAndUserId(long wishListId, long userId);

    boolean existsByUserIdAndProductIdAndOptionId(long userId, long productId, long optionId);

    boolean existsByUserIdAndProductIdAndOptionIdAndIdNot(long userId, long productId, long optionId, long wishListId);
}
