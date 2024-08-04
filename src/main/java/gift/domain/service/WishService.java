package gift.domain.service;

import gift.domain.dto.request.WishRequest;
import gift.domain.dto.response.WishResponse;
import gift.domain.entity.Member;
import gift.domain.entity.Product;
import gift.domain.entity.Wish;
import gift.domain.exception.ErrorCode;
import gift.domain.exception.ServerException;
import gift.domain.repository.WishRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;

    public WishService(WishRepository wishRepository, ProductService productService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<WishResponse> getWishlist(Member member) {
        return wishRepository.findWishesByMember(member).stream()
            .map(WishResponse::of)
            .toList();
    }

    @Transactional
    public WishResponse addWishlist(Member member, WishRequest wishRequest) {
        Product product = productService.getProductById(wishRequest.productId());

        wishRepository.findWishByMemberAndProduct(member, product).ifPresent(w -> {
            throw new ServerException(ErrorCode.PRODUCT_ALREADY_EXISTS_IN_WISHLIST);
        });

        return WishResponse.of(wishRepository.save(new Wish(product, member)));
    }

    @Transactional
    public void deleteWishlist(Member member, Long wishId) {
        Wish wish = findWishByIdAndMember(wishId, member);
        wishRepository.delete(wish);
    }

    public Wish findWishByIdAndMember(Long wishId, Member member) {
        Wish wish = wishRepository.findById(wishId).orElseThrow(() -> new ServerException(ErrorCode.WISH_NOT_FOUND));
        if (!wish.getMember().equals(member)) {
            throw new ServerException(ErrorCode.OTHER_MEMBERS_WISH_DELETION);
        }
        return wish;
    }
}
