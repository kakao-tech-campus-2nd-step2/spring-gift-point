package gift.wish.repository;

import gift.global.MyCrudRepository;
import gift.wish.domain.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WishRepository extends MyCrudRepository<Wish, Long> {
    List<Wish> findAllByMemberId(Long memberId);

    Page<Wish> findAllByMemberId(Long memberId, Pageable pageable);

    Optional<Wish> findByMemberIdAndProductId(Long memberId, Long productId);
}
