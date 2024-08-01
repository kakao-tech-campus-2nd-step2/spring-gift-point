package gift.repository;

import gift.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WishRepository extends JpaRepository<Wish, Long> {
    @Query("SELECT w FROM Wish w JOIN FETCH w.member JOIN FETCH w.product WHERE w.member.id = :memberId")
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);

    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}
