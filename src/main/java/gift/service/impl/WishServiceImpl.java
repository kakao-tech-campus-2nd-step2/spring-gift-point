package gift.service.impl;

import gift.model.Wish;
import gift.repository.WishRepository;
import gift.service.WishService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public Page<Wish> getWishesByMemberId(Long memberId, Pageable pageable) {
        return wishRepository.findAllByMemberId(memberId, pageable);
    }

    @Override
    public Wish addWish(Wish wish) {
        return wishRepository.save(wish);
    }

    @Override
    public boolean removeWish(Long id, Long memberId) {
        return wishRepository.findByIdAndMemberId(id, memberId)
            .map(wish -> {
                wishRepository.delete(wish);
                return true;
            })
            .orElse(false);
    }
}
