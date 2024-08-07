package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishProduct;
import gift.dto.response.MessageResponse;
import gift.dto.response.WishProductResponse;
import gift.exception.CustomException;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

import static gift.constant.SuccessMessage.ADD_SUCCESS_MSG;
import static gift.constant.SuccessMessage.DELETE_SUCCESS_MSG;
import static gift.exception.ErrorCode.DATA_NOT_FOUND;

@Service
public class WishListService {

    private final WishRepository wishRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishListService(WishRepository wishRepository, MemberRepository memberRepository, ProductRepository productRepository) {
        this.wishRepository = wishRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public Page<WishProductResponse> getWishList(Long memberId, Pageable pageable) {
        List<WishProductResponse> wishProducts = getWishProducts(memberId);
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), wishProducts.size());
        return new PageImpl<>(wishProducts.subList(start, end), pageable, wishProducts.size());
    }

    public MessageResponse addWishProduct(WishProduct wishProduct) {
        wishRepository.save(wish(wishProduct.getMemberId(), wishProduct.getProductId()));
        return new MessageResponse(ADD_SUCCESS_MSG);
    }

    public void deleteWishProduct(Long wishId) {
        wishRepository.deleteById(wishId);
    }

    private Wish wish(Long memberId, Long productId) {
        Member member = memberRepository.findMemberById(memberId).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        Product product = productRepository.findProductById(productId).orElseThrow(() -> new CustomException(DATA_NOT_FOUND));
        return new Wish(member, product);
    }

    private List<WishProductResponse> getWishProducts(Long memberId) {
        List<Wish> wishes = wishRepository.findWishByMemberId(memberId);
        return wishes.stream()
                .map(wish -> new WishProductResponse(wish.getId(), wish.getProduct())).toList();
    }
}
