package gift.service;

import gift.DTO.Wish.WishProductRequest;
import gift.DTO.Wish.WishProductResponse;
import gift.domain.Product;
import gift.domain.User;
import gift.domain.WishProduct;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishListRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishListService(
            WishListRepository wishListRepository, ProductRepository productRepository, UserRepository userRepository
    ){
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }
    /*
     * 특정 유저의 위시리스트를 오름차순으로 조회하는 로직
     */
    public Page<WishProductResponse> findWishList(Long id, Pageable pageable){
        Page<WishProduct> wishes = wishListRepository.findByUserId(id, pageable);
        return wishes.map(WishProductResponse::new);
    }
    /*
     * 특정 상품을 위시리스트에 추가하는 로직
     */
    @Transactional
    public WishProductResponse addWishList(WishProductRequest wishProductRequest){
        Long id = wishProductRequest.getUser().getId();
        Long productId = wishProductRequest.getProduct().getId();

        User savedUser = userRepository.findById(id).orElseThrow(NoSuchFieldError::new);
        Product savedProduct = productRepository.findById(productId).orElseThrow(NoSuchFieldError::new);
        WishProduct wishProduct = new WishProduct(savedUser, savedProduct);
        if(!wishListRepository.existsByUserIdAndProductId(id, productId))
            wishListRepository.save(wishProduct);

        return new WishProductResponse(wishProduct);
    }
    /*
     * 특정 유저의 특정 위시리스트 물품의 수량을 변경하는 로직
     */
    @Transactional
    public void updateWishProduct(Long userId, Long productId){
        WishProduct wish = wishListRepository.findByUserIdAndProductId(userId, productId);
        wishListRepository.save(wish);
    }
    /*
     * 특정 유저의 특정 위시리스트 물품을 삭제하는 로직
     */
    @Transactional
    public void deleteWishProduct(Long wishId){
        wishListRepository.deleteById(wishId);
    }

}
