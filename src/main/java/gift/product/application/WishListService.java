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
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public WishList getWishListByUserId(Long userId) {
        return wishListRepository.findByUserId(userId);
    }

    public Page<WishList> getProductsInWishList(Long userId, int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        if (wishListRepository.findByUserId(userId, pageable).isEmpty()) {
            throw new ProductException(ErrorCode.WISHLIST_NOT_FOUND);
        }
        return wishListRepository.findByUserId(userId, pageable);
    }

    @Transactional
    public void addProductToWishList(AddWishListRequest request) {
        WishList wishList = findById(request.getWishlistId());
        if (!Objects.equals(wishList.getUser().getId(), request.getUserId())) {
            throw new ProductException(ErrorCode.NOT_USER_OWNED);
        }

        Product product = productRepository.findById(request.getProductId());
        ProductOption productOption = productRepository.getProductWithOption(request.getProductId(), request.getOptionId());

        wishList.addWishListProduct(new WishListProduct(wishList, product, productOption, request.getQuantity()));


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
