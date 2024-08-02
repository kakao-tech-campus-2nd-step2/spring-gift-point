package gift.repository;

import gift.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WishRepository extends JpaRepository<Wish, Long> {
    Page<Wish> findByMemberId(Long memberId, Pageable pageable);
}
