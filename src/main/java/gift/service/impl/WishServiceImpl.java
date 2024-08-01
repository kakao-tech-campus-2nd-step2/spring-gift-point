package gift.service.impl;

import gift.model.Wish;
import gift.repository.WishRepository;
import gift.service.WishService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WishServiceImpl implements WishService {

    private final WishRepository wishRepository;

    public WishServiceImpl(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    @Override
    public List<Wish> getWishesByMemberId(Long memberId) {
        return wishRepository.findByMemberId(memberId);
    }

    @Override
    public Wish addWish(Wish wish) {
        return wishRepository.save(wish);
    }

    @Override
    public boolean removeWish(Long id, Long memberId) {
        if (wishRepository.existsById(id)) {
            wishRepository.deleteByIdAndMemberId(id, memberId);
            return true;
        }
        return false;
    }
}