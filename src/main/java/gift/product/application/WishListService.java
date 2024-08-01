package gift.product.application;

import gift.product.domain.*;
import gift.product.exception.ProductException;
import gift.product.infra.ProductRepository;
import gift.product.infra.WishListRepository;
import gift.user.application.UserService;
import gift.user.domain.User;
import gift.util.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
            return new ArrayList<>();
        }
        Page<WishList> wishLists = wishListRepository.findByUserId(userId, pageable);
        return wishLists.map(wishList -> {
            return new WishListResponse(wishList.getId(), wishList.getWishListProduct());
        }).getContent();
    }

    @Transactional
    public void addProductToWishList(AddWishListRequest request) {
        WishList wishList = new WishList(userService.getUser(request.getUserId()), LocalDateTime.now());
        Product product = productRepository.findById(request.getProductId());
        wishList.addWishListProduct(new WishListProduct(wishList, product, 1L));
        wishListRepository.save(wishList);
    }

    public WishList findById(Long id) {
        return wishListRepository.findById(id);
    }

    @Transactional
    public void deleteProductFromWishList(Long userId, Long wishListId) {
        List<WishList> wishList = wishListRepository.findByUserId(userId);
        if (wishList.isEmpty()) {
            return;
        }

        WishList targetWishList = wishList.stream()
                .filter(w -> Objects.equals(w.getId(), wishListId))
                .findFirst()
                .orElseThrow(() -> new ProductException(ErrorCode.WISHLIST_NOT_FOUND));

        wishListRepository.delete(targetWishList);
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
