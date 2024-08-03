package gift.doamin.wishlist.service;

import gift.doamin.product.entity.Option;
import gift.doamin.product.exception.ProductNotFoundException;
import gift.doamin.product.repository.JpaProductRepository;
import gift.doamin.product.repository.OptionRepository;
import gift.doamin.user.entity.User;
import gift.doamin.user.exception.UserNotFoundException;
import gift.doamin.user.repository.JpaUserRepository;
import gift.doamin.wishlist.dto.WishRequest;
import gift.doamin.wishlist.dto.WishResponse;
import gift.doamin.wishlist.entity.Wish;
import gift.doamin.wishlist.exception.InvalidWishFormException;
import gift.doamin.wishlist.exception.WishListNotFoundException;
import gift.doamin.wishlist.repository.JpaWishListRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishListService {

    private final JpaWishListRepository wishListRepository;
    private final JpaUserRepository UserRepository;
    private final JpaProductRepository productRepository;
    private final OptionRepository optionRepository;

    public WishListService(JpaWishListRepository wishListRepository,
        JpaUserRepository UserRepository, JpaProductRepository productRepository,
        OptionRepository optionRepository) {
        this.wishListRepository = wishListRepository;
        this.UserRepository = UserRepository;
        this.productRepository = productRepository;
        this.optionRepository = optionRepository;
    }

    @Transactional
    public WishResponse create(Long userId, WishRequest wishRequest) {
        Long optionId = wishRequest.getOptionId();
        if (wishListRepository.existsByUserIdAndOptionId(userId, optionId)) {
            throw new InvalidWishFormException("동일한 상품을 위시리스트에 또 넣을수는 없습니다");
        }
        // 수량 0개는 등록 불가
        if (wishRequest.isZeroQuantity()) {
            throw new InvalidWishFormException("위시리스트에 상품 0개를 넣을수는 없습니다");
        }

        User user = UserRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        Option option = optionRepository.findById(optionId).orElseThrow(
            ProductNotFoundException::new);

        Wish wish = wishListRepository.save(new Wish(user, option, wishRequest.getQuantity()));

        return new WishResponse(wish);
    }

    public Page<WishResponse> getPage(Long userId, Pageable pageable) {

        return wishListRepository.findAllByUserId(userId, pageable).map(WishResponse::new);
    }

    @Transactional
    public WishResponse update(Long userId, WishRequest wishRequest) {
        Long optionId = wishRequest.getOptionId();

        // 존재하지 않는 옵션 id일경우 400 에러
        if (!optionRepository.existsById(wishRequest.getOptionId())) {
            throw new IllegalArgumentException("해당 옵션이 존재하지 않습니다.");
        }

        // 수량을 지정하지 않았거나 0으로 수정하는 경우 위시리스트에서 삭제
        if (wishRequest.isZeroQuantity()) {
            delete(userId, optionId);
            return null;
        }

        // 해당 위시리스트가 존재하지 않으면(사용자의 위시리스트에 해당 상품이 없으면) 새로 생성
        Optional<Wish> target = wishListRepository.findByUserIdAndOptionId(userId, optionId);
        if (target.isEmpty()) {
            return create(userId, wishRequest);
        }

        Wish wish = target.get();

        wish.updateQuantity(wishRequest.getQuantity());

        return new WishResponse(wish);
    }

    @Transactional
    public void delete(Long userId, Long optionId) {
        Wish wish = wishListRepository.findByUserIdAndOptionId(userId, optionId)
            .orElseThrow(() -> new WishListNotFoundException("위시리스트에 삭제할 상품이 존재하지 않습니다."));

        wishListRepository.deleteById(wish.getId());
    }
}
