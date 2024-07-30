package gift.service;

import gift.common.dto.PageResponse;
import gift.common.exception.ErrorCode;
import gift.common.exception.ProductException;
import gift.common.exception.UserException;
import gift.common.exception.WishException;
import gift.controller.wish.dto.WishRequest;
import gift.controller.wish.dto.WishResponse;
import gift.model.Product;
import gift.model.User;
import gift.model.Wish;
import gift.repository.ProductRepository;
import gift.repository.UserRepository;
import gift.repository.WishRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository,
        UserRepository userRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    public PageResponse<WishResponse> findAllWish(Long userId, Pageable pageable) {
        Page<Wish> wishList = wishRepository.findByUserId(userId, pageable);

        List<WishResponse> wishResponses = wishList.getContent().stream()
            .map(WishResponse::from)
            .toList();

        return PageResponse.from(wishResponses, wishList);
    }

    @Transactional
    public Long addWistList(Long userId, WishRequest.Create request) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));

        Product product = productRepository.findById(request.productId())
            .orElseThrow(() -> new ProductException(ErrorCode.PRODUCT_NOT_FOUND));

        if (wishRepository.existsByProductIdAndUserId(product.getId(), userId)) {
            throw new WishException(ErrorCode.EXIST_WISH);
        }

        Wish wish = wishRepository.save(request.toEntity(user, product));
        return wish.getId();
    }

    @Transactional
    public void updateWishList(Long userId, Long wishId, WishRequest.Update request) {
        if (request.count() == 0) {
            deleteWishList(userId, wishId);
            return;
        }

        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new WishException(ErrorCode.WISH_NOT_FOUND));

        if (!wish.isOwner(userId)) {
            throw new WishException(ErrorCode.WISH_NOT_FOUND);
        }

        //wish.updateWish(request.count());
    }

    @Transactional
    public void deleteWishList(Long userId, Long wishId) {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new WishException(ErrorCode.WISH_NOT_FOUND));

        if (!wish.isOwner(userId)) {
            throw new WishException(ErrorCode.WISH_NOT_FOUND);
        }

        wishRepository.deleteById(wishId);
    }
}
