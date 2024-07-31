package gift.service;

import gift.controller.dto.ProductResponse;
import gift.controller.dto.WishRequest;
import gift.controller.dto.WishResponse;
import gift.domain.Product;
import gift.domain.UserInfo;
import gift.domain.Wish;
import gift.repository.ProductRepository;
import gift.repository.UserInfoRepository;
import gift.repository.WishRepository;
import gift.utils.error.ProductNotFoundException;
import gift.utils.error.UserNotFoundException;
import gift.utils.error.WishListNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final UserInfoRepository userInfoRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, UserInfoRepository userInfoRepository,
        ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.userInfoRepository = userInfoRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public WishResponse addToWishlist(String email, WishRequest wishRequest) {
        Product product = productRepository.findById(wishRequest.getProductId()).orElseThrow(
            () -> new ProductNotFoundException("Product Not Found")
        );
        UserInfo byEmail = userInfoRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User Not Found"));
        Wish wish = new Wish(product, byEmail, wishRequest.getCount());

        product.addWish(wish);
        byEmail.addWish(wish);

        wishRepository.save(wish);
        return new WishResponse(wish.getId(),wish.getProduct().getId(),wish.getCount());

    }
    @Transactional
    public WishResponse removeFromWishlist(String email, Long wishId) {

        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );

        Wish wish = wishRepository.findById(wishId).orElseThrow(
            () -> new WishListNotFoundException("Wish Not Found")
        );

        if (userInfo.getId().equals(wish.getProduct().getId())){
            throw new ()
        }

        wishRepository.deleteByProductIdAndUserInfoId(wish.getProduct().getId(), wish.getUserInfo().getId());
        return new WishResponse(wish.getId(),wish.getProduct().getId(),wish.getCount());

    }

    public Page<WishResponse> getWishlistProducts(String email, Pageable pageable) {
        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        Page<Wish> byUserInfoId = wishRepository.findByUserInfoId(userInfo.getId(), pageable);
        return byUserInfoId.map(this::convertToWishResponse);
    }
    @Transactional
    public WishResponse changeToWishlist(String email, WishRequest wishRequest) {
        Product product = productRepository.findById(wishRequest.getProductId()).orElseThrow(
            () -> new ProductNotFoundException("Product Not Found")
        );
        UserInfo userInfo = userInfoRepository.findByEmail(email).orElseThrow(
            () -> new UserNotFoundException("User Not Found")
        );
        Wish existingWish = wishRepository.findByUserInfoIdAndProductId(userInfo.getId(),
            product.getId()).orElseThrow(
            () -> new WishListNotFoundException("Wish Not Found")
        );

        if (wishRequest.getCount() == 0) {
            if (existingWish != null) {
                product.removeWish(existingWish);
                userInfo.removeWish(existingWish);
                wishRepository.delete(existingWish);
            }
            return new WishResponse(existingWish.getId(),existingWish.getProduct().getId(),existingWish.getCount());
        }
        if (existingWish == null) {
            throw new ProductNotFoundException("Product Not Found");
        }
        existingWish.setCount(wishRequest.getCount());
        return new WishResponse(existingWish.getId(),existingWish.getProduct().getId(),existingWish.getCount());
    }

    private WishResponse convertToWishResponse(Wish wish){
        return new WishResponse(wish.getId(),wish.getProduct().getId(),wish.getCount());
    }


}
