package gift.service;

import gift.dto.WishResponse;
import gift.model.Wish;
import gift.repository.WishRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService {

    private final WishRepository wishRepository;

    @Autowired
    public WishService(WishRepository wishRepository) {
        this.wishRepository = wishRepository;
    }

    public Page<WishResponse> getWishesByMemberId(Long memberId, Pageable pageable) {
        Page<Wish> wishes = wishRepository.findByMemberId(memberId, pageable);
        return wishes.map(this::convertToWishResponse);
    }

    public Wish addWish(Wish wish) {
        return wishRepository.save(wish);
    }

    public void deleteWish(Long wishId) {
        if (!wishRepository.existsById(wishId)) {
            throw new IllegalArgumentException("Wish not found with id: " + wishId);
        }
        wishRepository.deleteById(wishId);
    }

    private WishResponse convertToWishResponse(Wish wish) {
        WishResponse wishResponse = new WishResponse();
        wishResponse.setId(wish.getId());
        wishResponse.setProductName(wish.getProduct().getName());
        wishResponse.setProductPrice(wish.getProduct().getPrice());
        wishResponse.setProductImageurl(wish.getProduct().getImageurl());
        wishResponse.setProductCategory(wish.getProduct().getCategory().getName());
        wishResponse.setOptionName(wish.getProductOption().getName());
        return wishResponse;
    }

    public void deleteWishByProductOptionIdAndMemberId(Long optionId, Long memberId) {
        Wish wish = wishRepository.findByProductOptionIdAndMemberId(optionId, memberId);
        if (wish != null) {
            wishRepository.delete(wish);
        }
    }
}