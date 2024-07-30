package gift.product.application;

import gift.product.domain.*;
import gift.product.exception.ProductException;
import gift.product.infra.ProductRepository;
import gift.product.infra.WishListRepository;
import gift.user.application.UserService;
import gift.user.domain.User;
import gift.util.ErrorCode;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserService userService;

    public WishListService(WishListRepository wishListRepository, ProductRepository productRepository,
                           UserService userService) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.userService = userService;
    }


    public List<WishListResponse> getWishListByUserId(Long userId, Pageable pageable) {
        if (wishListRepository.findByUserId(userId, pageable).isEmpty()) {
            wishListRepository.save(new WishList(userService.getUser(userId), LocalDateTime.now()));
        }
        Page<WishList> wishLists = wishListRepository.findByUserId(userId, pageable);

        return wishLists.stream()
                .map(wishList -> new WishListResponse(wishList.getId(),
                        wishList.getWishListProducts().stream()
                                .map(WishListProduct::getProduct)
                                .map(ProductResponse::new)
                                .collect(Collectors.toList())))
                .collect(Collectors.toList());
    }


    @Transactional
    public void addProductToWishList(AddWishListRequest request) {
        WishList wishList = findById(request.getWishlistId());
        if (!Objects.equals(wishList.getUser().getId(), request.getUserId())) {
            throw new ProductException(ErrorCode.NOT_USER_OWNED);
        }
        Product product = productRepository.findById(request.getProductId());
        wishList.addWishListProduct(new WishListProduct(wishList, product, request.getQuantity()));


        wishListRepository.save(wishList);
    }

    public WishList findById(Long id) {
        return wishListRepository.findById(id);
    }

    @Transactional
    public void deleteProductFromWishList(Long userId, Long optionId) {
        WishList wishList = wishListRepository.findByUserId(userId);
        if (wishList != null) {
            wishList.deleteProduct(optionId);
            wishListRepository.save(wishList);
        } else {
            throw new ProductException(ErrorCode.WISHLIST_NOT_FOUND);
        }
    }

    public void createWishList(Long userId) {
        if (wishListRepository.findByUserId(userId) != null) {
            throw new ProductException(ErrorCode.WISHLIST_ALREADY_EXISTS);
        }
        User user = userService.getUser(userId);

        WishList wishList = new WishList(user, LocalDateTime.now());
        wishListRepository.save(wishList);
    }
}
