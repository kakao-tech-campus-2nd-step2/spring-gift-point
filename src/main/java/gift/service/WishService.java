package gift.service;

import gift.domain.Wish;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class WishService {
    private final WishRepository wishRepository;

    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Transactional(readOnly = true)
    public List<Wish> getWishesByMemberId(Long memberId) {
        return wishRepository.findAllByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public Page<Wish> getWishesByMemberId(Long memberId, Pageable pageable) {
        return wishRepository.findAllByMemberId(memberId, pageable);
    }

    @Transactional
    public void addWish(Wish wish) {
        wishRepository.save(wish);
    }

    @Transactional
    public void removeWish(Long memberId, Long productId) {
        wishRepository.deleteByMemberIdAndProductId(memberId, productId);
    }
}
