package gift.repository;

import gift.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WishlistRepository extends JpaRepository<Wish, Long> {
    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
    List<Wish> findByMemberId(Long memberId);
    Page<Wish> findByMemberId(Long memberId, PageRequest pageRequest);
}
