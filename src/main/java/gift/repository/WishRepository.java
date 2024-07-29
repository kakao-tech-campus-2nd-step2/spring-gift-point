package gift.repository;

import gift.entity.Wish;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishRepository extends JpaRepository<Wish, Long>,
    PagingAndSortingRepository<Wish, Long> {

    Page<Wish> findByMemberId(Long memberId, Pageable pageable);


    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
}