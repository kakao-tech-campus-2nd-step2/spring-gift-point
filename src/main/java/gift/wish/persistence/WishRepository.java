package gift.wish.persistence;

import gift.wish.domain.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query("SELECT w FROM Wish w JOIN FETCH w.member JOIN FETCH w.product WHERE w.id = :wishId")
    Optional<Wish> findWishByIdWithUserAndProduct(Long wishId);

    @Query("SELECT w FROM Wish w JOIN FETCH w.member JOIN FETCH w.product WHERE w.member.id = :memberId")
    Page<Wish> findWishesByUserIdWithMemberAndProduct(Long memberId, Pageable pageable);

    Optional<Wish> findWishByMemberIdAndProductId(Long memberId, Long productId);
}
