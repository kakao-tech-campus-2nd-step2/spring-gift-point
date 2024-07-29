package gift.repository;

import gift.domain.Wish;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;

public interface WishRepository extends JpaRepository<Wish,Long> {
    Optional<List<Wish>> findByMemberId(Long memberId);
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
    Page<Wish> findAllByMemberIdOrderByCreatedAt(Long memberId, Pageable pageable);
}
