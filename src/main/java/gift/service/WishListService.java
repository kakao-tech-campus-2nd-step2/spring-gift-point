package gift.service;

import gift.domain.AppUser;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.wish.AddWishRequest;
import gift.dto.wish.WishListResponse;
import gift.repository.WishListRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final UserService userService;
    private final ProductService productService;

    public WishListService(WishListRepository wishListRepository, UserService userService,
                           ProductService productService) {
        this.wishListRepository = wishListRepository;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public Page<WishListResponse> getWishList(Long userId, Pageable pageable) {
        Page<Wish> wishes = wishListRepository.findWishesByAppUserId(userId, pageable);
        return wishes.map(w -> new WishListResponse(
                w.getId(),
                w.getProduct().getId(),
                w.getProduct().getName(),
                w.getProduct().getPrice(),
                w.getProduct().getImageUrl(),
                w.getQuantity()));
    }

    @Transactional
    public void addWish(Long userId, AddWishRequest addWishRequest) {
        AppUser appUser = userService.findUser(userId);
        Product product = productService.findProduct(addWishRequest.productId());
        Wish wish = new Wish(appUser, product, addWishRequest.quantity());
        wishListRepository.save(wish);
    }

    @Transactional
    public void updateWishQuantity(Long userId, Long wishId, int quantity) {
        Wish wish = wishListRepository.findByIdAndAppUserId(wishId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Wish"));
        wish.updateQuantity(quantity);
        wishListRepository.save(wish);
    }

    @Transactional
    public void deleteWish(Long userId, Long wishId) {
        Wish wish = wishListRepository.findByIdAndAppUserId(wishId, userId)
                .orElseThrow(() -> new EntityNotFoundException("Wish"));
        wishListRepository.delete(wish);
    }

    @Transactional
    public void deleteWishIfExists(Long userId, Long productId) {
        wishListRepository.findByProductIdAndAppUserId(productId, userId)
                .ifPresent(wishListRepository::delete);
    }
}
