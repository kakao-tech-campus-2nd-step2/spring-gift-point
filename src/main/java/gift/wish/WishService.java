package gift.wish;

import gift.common.auth.LoginMemberDto;
import gift.common.exception.ProductException;
import gift.common.exception.WishException;
import gift.product.ProductErrorCode;
import gift.product.ProductRepository;
import gift.product.model.Product;
import gift.wish.model.Wish;
import gift.wish.model.WishRequest;
import gift.wish.model.WishResponse;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductRepository productRepository;

    public WishService(WishRepository wishRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public List<WishResponse> getWishList(LoginMemberDto loginMemberDto, Pageable pageable) {
        return wishRepository.findAllByMemberId(loginMemberDto.getId(),
                pageable)
            .map(WishResponse::from)
            .getContent();
    }

    @Transactional
    public Long addProductToWishList(WishRequest wishRequest, LoginMemberDto loginMemberDto)
        throws ProductException {
        Product product = productRepository.findById(wishRequest.productId())
            .orElseThrow(() -> new ProductException(ProductErrorCode.NOT_FOUND));
        Wish wish = new Wish(loginMemberDto.toEntity(), product, wishRequest.count());
        wishRepository.save(wish);
        return wish.getId();
    }

    @Transactional
    public void updateProductInWishList(Long wishId, WishRequest wishRequest,
        LoginMemberDto loginMemberDto) throws WishException {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new WishException(WishErrorCode.NOT_FOUND));
        wish.validateMember(loginMemberDto.getId());
        if (wishRequest.isCountZero()) {
            wishRepository.deleteById(wishId);
            return;
        }
        wish.changeCount(wishRequest.count());
    }

    @Transactional
    public void deleteProductInWishList(Long wishId,
        LoginMemberDto loginMemberDto) throws WishException {
        Wish wish = wishRepository.findById(wishId)
            .orElseThrow(() -> new WishException(WishErrorCode.NOT_FOUND));
        wish.validateMember(loginMemberDto.getId());
        wishRepository.deleteById(wishId);
    }
}
