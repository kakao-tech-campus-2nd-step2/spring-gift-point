package gift.service;

import gift.common.exception.WishListNotFoundException;
import gift.model.product.Product;
import gift.model.user.User;
import gift.model.wishlist.WishList;
import gift.model.wishlist.WishRequest;
import gift.model.wishlist.WishResponse;
import gift.repository.product.ProductRepository;
import gift.repository.user.UserRepository;
import gift.repository.wish.WishListRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishListService(WishListRepository wishListRepository,
        ProductRepository productRepository,
        UserRepository userRepository) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Page<WishResponse> getWishListByUserId(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow();
        return wishListRepository.findAllByUser(user, pageable)
            .map(wishList -> WishResponse.from(wishList, wishList.getProduct()));
    }

    public WishResponse addWishList(Long userId, WishRequest wishRequest) {
        User user = userRepository.findById(userId).orElseThrow();
        Product product = productRepository.findById(wishRequest.productId()).orElseThrow();
        WishList wishList = wishRequest.toEntity(user, product);
        return WishResponse.from(wishListRepository.save(wishList), product);

    }

    public WishResponse updateProductQuantity(Long wishId, Long userId, int quantity) {
        WishList wishList = wishListRepository.findByIdAndUserId(wishId, userId).orElseThrow(
            () -> new WishListNotFoundException("해당 Id의 위시리스트는 존재하지 않습니다.")
        );
        wishList.setQuantity(quantity);
        return WishResponse.from(wishListRepository.save(wishList), wishList.getProduct());
    }


    public void removeWishList(Long userId, Long productId) {
        wishListRepository.deleteByUserIdAndAndProductId(userId, productId);
    }
}