package gift.repository;

import gift.model.entity.WishItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WishListRepository extends JpaRepository<WishItem, Long> {

    @Query("select w from WishItem w join fetch w.item where w.user.id =:userId")
    Page<WishItem> findAllByUserIdFetchJoin(@Param("userId") Long userId, Pageable pageable);

    Boolean existsByUserIdAndItemId(Long userId, Long itemId);

    void deleteByUserIdAndItemId(Long userId, Long itemId);
}
