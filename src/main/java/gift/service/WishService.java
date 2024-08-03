package gift.service;

import gift.model.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WishService {
    Page<Wish> getWishesByMemberId(Long memberId, Pageable pageable);
    Wish addWish(Wish wish);
    boolean removeWish(Long id, Long memberId);
}
