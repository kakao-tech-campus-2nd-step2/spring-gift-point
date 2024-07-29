package gift.service;

import gift.entity.Member;
import gift.entity.Wish;
import gift.exception.CustomException;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WishService {
    private final WishRepository wishRepository;

    @Autowired
    public WishService (WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public Wish save(Wish wish) {
        return wishRepository.save(wish);
    }

    public List<Wish> findByMember(Member member) {
        return wishRepository.findByMember(member);
    }

    public Optional<Wish> findById(Long id) {
        return wishRepository.findById(id);
    }

    public void deleteById(Long id) {
        wishRepository.deleteById(id);
    }

    public void deleteByProductId(Long productId) {
        wishRepository.deleteByProduct_Id(productId);
    }

    public Wish updateWish(Long wishId) {
        Wish wish = wishRepository.findById(wishId).orElseThrow(() -> new CustomException.EntityNotFoundException("Wish not found"));
        return wishRepository.save(wish);
    }
}
