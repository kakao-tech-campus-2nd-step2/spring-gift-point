package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.OrderResponse;
import gift.main.dto.UserVo;
import gift.main.dto.WishProductResponse;
import gift.main.entity.Product;
import gift.main.entity.User;
import gift.main.entity.WishProduct;
import gift.main.repository.ProductRepository;
import gift.main.repository.UserRepository;
import gift.main.repository.WishProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishProductService {

    private static final int PAGE_SIZE = 20;

    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishProductService(WishProductRepository wishProductRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.wishProductRepository = wishProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Page<WishProductResponse> getWishProductPage(UserVo sessionUser, int pageNum) {
        Pageable pageable = PageRequest.of(pageNum, PAGE_SIZE);
        return wishProductRepository.findAllByUserId(sessionUser.getId(), pageable)
                .map(wishProduct -> new WishProductResponse(wishProduct));
    }

    @Transactional
    public void addWishlistProduct(Long productId, UserVo sessionUser) {
        Product product = validateProduct(productId);
        User user = validateUser(sessionUser);
        boolean wishListExists = wishProductRepository.existsByProductIdAndUserId(productId, sessionUser.getId());

        if (wishListExists) {
            throw new CustomException(ErrorCode.ALREADY_EXISTING_WISH_LIST);
        }
        wishProductRepository.save(new WishProduct(product, user));
    }

    @Transactional
    public void deleteWishProduct(Long wishId) {
        wishProductRepository.deleteById(wishId);
    }

    //주문으로 인한 위시 프로덕트 삭제 로직
    @Transactional
    public void deleteWishProductsFromOrders(OrderResponse orderResponse) {
        //위시리스트를 삭제해야한다.. 위시리스트..
        if (wishProductRepository.existsByProductIdAndUserId(orderResponse.productId(), orderResponse.buyerId())) {
            return;
        }
        WishProduct wishProduct = wishProductRepository.findByProductIdAndUserId(orderResponse.productId(), orderResponse.buyerId()).get();
        wishProductRepository.delete(wishProduct);

    }

    private Product validateProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
    }

    private User validateUser(UserVo sessionUser) {
        return userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
    }


}
