package gift.repository;

import gift.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {

    @Query("SELECT w FROM Wish w JOIN FETCH w.product p JOIN FETCH w.member m WHERE m.id= :memberId")
    Page<Wish> findAllByMemberIdFetchJoin(Long memberId, Pageable pageable);

    @Query("SELECT w FROM Wish w JOIN FETCH w.product JOIN FETCH w.member WHERE w.id= :id")
    Optional<Wish> findByIdFetchJoin(Long id);

    void deleteByProductIdAndMemberId(Long productId, Long memberId);

    boolean existsByProductIdAndMemberId(Long productId, Long memberId);

}
