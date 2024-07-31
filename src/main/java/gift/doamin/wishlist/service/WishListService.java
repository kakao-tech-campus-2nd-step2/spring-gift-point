package gift.doamin.wishlist.service;

import gift.doamin.product.entity.Product;
import gift.doamin.product.exception.ProductNotFoundException;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.wishlist.dto.WishForm;
import gift.doamin.wishlist.dto.WishParam;
import gift.doamin.wishlist.entity.Wish;
import gift.doamin.wishlist.exception.InvalidWishFormException;
import gift.doamin.wishlist.exception.WishListNotFoundException;
import gift.doamin.wishlist.repository.JpaWishListRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishListService {

    private final JpaWishListRepository wishListRepository;
    private final JpaUserRepository UserRepository;
    private final JpaProductRepository productRepository;

    public WishListService(JpaWishListRepository wishListRepository,
        JpaUserRepository UserRepository, JpaProductRepository productRepository) {
        this.wishListRepository = wishListRepository;
        this.UserRepository = UserRepository;
        this.productRepository = productRepository;
    }

    public void create(Long userId, WishForm wishForm) {
        Long productId = wishForm.getProductId();
        if (wishListRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new InvalidWishFormException("동일한 상품을 위시리스트에 또 넣을수는 없습니다");
        }
        // 수량 0개는 등록 불가
        if (wishForm.isZeroQuantity()) {
            throw new InvalidWishFormException("위시리스트에 상품 0개를 넣을수는 없습니다");
        }

        User user = UserRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Product product = productRepository.findById(productId).orElseThrow(
            ProductNotFoundException::new);

        wishListRepository.save(new Wish(user, product, wishForm.getQuantity()));
    }

    public Page<WishParam> getPage(Long userId, int pageNum) {

        Pageable pageable = PageRequest.of(pageNum, 5);

        return wishListRepository.findAllByUserId(userId, pageable).map(WishParam::new);
    }

    public void update(Long userId, WishForm wishForm) {
        Long productId = wishForm.getProductId();

        // 수량을 지정하지 않았거나 0으로 수정하는 경우 위시리스트에서 삭제
        if (wishForm.isZeroQuantity()) {
            delete(userId, productId);
            return;
        }

        // 해당 위시리스트가 존재하지 않으면(사용자의 위시리스트에 해당 상품이 없으면) 새로 생성
        Optional<Wish> target = wishListRepository.findByUserIdAndProductId(userId,
            productId);
        if (target.isEmpty()) {
            create(userId, wishForm);
            return;
        }

        Wish wish = target.get();

        wish.updateQuantity(wishForm.getQuantity());
        wishListRepository.save(wish);
    }

    public void delete(Long userId, Long productId) {
        Wish wish = wishListRepository.findByUserIdAndProductId(userId, productId)
            .orElseThrow(() -> new WishListNotFoundException("위시리스트에 삭제할 상품이 존재하지 않습니다."));

        wishListRepository.deleteById(wish.getId());
    }
}
