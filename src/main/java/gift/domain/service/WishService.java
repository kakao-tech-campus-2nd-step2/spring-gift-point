package gift.domain.service;

import gift.domain.dto.request.WishDeleteRequest;
import gift.domain.dto.request.WishRequest;
import gift.domain.dto.response.WishAddResponse;
import gift.domain.dto.response.WishResponse;
import gift.domain.entity.Member;
import gift.domain.entity.Product;
import gift.domain.entity.Wish;
import gift.domain.exception.notFound.ProductNotIncludedInWishlistException;
import gift.domain.repository.WishRepository;
import gift.global.WebConfig.Constants.Domain.Wish.QuantityUpdateAction;
import java.util.List;
import java.util.Optional;
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

    public Wish findWishByMemberAndProduct(Member member, Product product) {
        return wishRepository.findWishByMemberAndProduct(member, product)
            .orElseThrow(ProductNotIncludedInWishlistException::new);
    }

    @Transactional(readOnly = true)
    public List<WishResponse> getWishlist(Member member) {
        return wishRepository.findWishesByMember(member).stream()
            .map(wish -> WishResponse.of(wish.getQuantity(), wish.getProduct()))
            .toList();
    }

    @Transactional
    public WishAddResponse addWishlist(Member member, WishRequest wishRequest) {
        Product product = productService.getProductById(wishRequest.productId());
        Optional<Wish> search = wishRepository.findWishByMemberAndProduct(member, product);

        //아이템이 없고 수량이 1 이상일 때 새 데이터 삽입
        if (search.isEmpty()) {
            if (wishRequest.quantity() <= 0) {
                // 0 이하인 경우 아무 작업 하지 않음
                return new WishAddResponse(QuantityUpdateAction.NOPE.toString(), 0L);
            }
            wishRepository.save(new Wish(product, member, wishRequest.quantity()));
            return new WishAddResponse(QuantityUpdateAction.CREATE.toString(), wishRequest.quantity());
        }

        Wish wish = search.get();

        //수량은 최소한 0 이상이어야 함
        wish.setQuantity(wishRequest.quantity() + wish.getQuantity());

        //업데이트 후 수량이 음수면 delete 수행
        if (wish.getQuantity() <= 0) {
            wishRepository.delete(wish);
            return new WishAddResponse(QuantityUpdateAction.DELETE.toString(), 0L);
        }

        return new WishAddResponse(QuantityUpdateAction.ADD.toString(), wish.getQuantity());
    }

    @Transactional
    public WishResponse updateWishlist(Member member, WishRequest wishRequest) {
        Product product = productService.getProductById(wishRequest.productId());
        Wish wish = findWishByMemberAndProduct(member, product);
        wish.set(wishRequest);
        return WishResponse.of(wish.getQuantity(), product);
    }

    @Transactional
    public void deleteWishlist(Member member, WishDeleteRequest deleteRequestDto) {
        Product product = productService.getProductById(deleteRequestDto.productId());
        findWishByMemberAndProduct(member, product);
        wishRepository.deleteByMemberAndProduct(member, product);
    }
}
