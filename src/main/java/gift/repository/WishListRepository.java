package gift.repository;

import gift.model.WishList;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Long> {

    Page<WishList> findWishListByMemberId(long memberId, Pageable pageable);

    Optional<WishList> findByMemberIdAndProductId(long memberId, long productId);
}