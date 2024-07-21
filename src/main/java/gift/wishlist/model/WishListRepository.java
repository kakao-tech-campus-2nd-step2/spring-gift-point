package gift.wishlist.model;

import gift.wishlist.model.dto.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<Wish, Long> {
    List<Wish> findWishesByAppUserId(@Param("userId") Long userId);

    Optional<Wish> findByIdAndAppUserId(Long id, Long userId);

    Page<Wish> findWishesByAppUserId(@Param("userId") Long userId, Pageable pageable);
}
