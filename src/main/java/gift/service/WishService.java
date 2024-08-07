package gift.service;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Wish;
import gift.exception.CustomException;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public Page<Wish> findByMember(Member member, Pageable pageable) {
        return wishRepository.findByMember(member, pageable);
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

    public Wish createWish(Member member,Product product, int quantity) {
        Optional<Wish> existingWish = wishRepository.findByMemberAndProduct(member, product);
        if(existingWish.isPresent()) {
            throw new CustomException.EntityAlreadyExistException("Wish already exist for this product");
        }
        Wish wish = new Wish.Builder()
                .member(member)
                .product(product)
                .build();
        wish.update(quantity);
        return wishRepository.save(wish);
    }

}
