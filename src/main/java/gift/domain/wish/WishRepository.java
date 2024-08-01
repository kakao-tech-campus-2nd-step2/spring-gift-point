package gift.domain.wish;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends JpaRepository<Wish, Long> {
    void deleteByMemberIdAndProductId(Long memberId, Long productId);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);

    List<Wish> findAllByMemberId(Long id, Pageable pageable);
}
