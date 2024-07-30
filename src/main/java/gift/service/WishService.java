package gift.service;

import gift.model.Wish;
import java.util.List;

public interface WishService {
    List<Wish> getWishesByMemberId(Long memberId);
    Wish addWish(Wish wish);
    boolean removeWish(Long id, Long memberId);
}
