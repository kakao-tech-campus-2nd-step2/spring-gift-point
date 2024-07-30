package gift.main.service;

import gift.main.Exception.CustomException;
import gift.main.Exception.ErrorCode;
import gift.main.dto.UserVo;
import gift.main.dto.WishProductResponse;
import gift.main.entity.Product;
import gift.main.entity.User;
import gift.main.entity.WishProduct;
import gift.main.repository.ProductRepository;
import gift.main.repository.UserRepository;
import gift.main.repository.WishProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishProductService {

    private final WishProductRepository wishProductRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishProductService(WishProductRepository wishProductRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.wishProductRepository = wishProductRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Page<WishProductResponse> getWishProductPage(UserVo sessionUser, Pageable pageable) {
        Page<WishProductResponse> wishProductResponsePage = wishProductRepository.findAllByUserId(sessionUser.getId(), pageable)
                .map(wishProduct -> new WishProductResponse(wishProduct));
        return wishProductResponsePage;

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
    public void deleteProducts(Long productId, UserVo sessionUserVo) {
        wishProductRepository.deleteByProductIdAndUserId(productId, sessionUserVo.getId());
    }

    private Product validateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_PRODUCT));
        return product;
    }

    private User validateUser(UserVo sessionUser) {
        User user = userRepository.findByEmail(sessionUser.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        return user;
    }


}
