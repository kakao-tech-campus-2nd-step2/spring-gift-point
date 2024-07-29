package gift.wishlist.application;

import gift.global.error.CustomException;
import gift.global.error.ErrorCode;
import gift.member.dao.MemberRepository;
import gift.member.entity.Member;
import gift.product.dao.ProductRepository;
import gift.product.dto.ProductResponse;
import gift.product.entity.Product;
import gift.product.util.ProductMapper;
import gift.wishlist.dao.WishesRepository;
import gift.wishlist.entity.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishesService {

    private final WishesRepository wishesRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    public WishesService(WishesRepository wishesRepository,
                         MemberRepository memberRepository,
                         ProductRepository productRepository) {
        this.wishesRepository = wishesRepository;
        this.memberRepository = memberRepository;
        this.productRepository = productRepository;
    }

    public void addProductToWishlist(Long memberId, Long productId) {
        wishesRepository.findByMember_IdAndProduct_Id(memberId, productId)
                .ifPresent(wish -> {
                    throw new CustomException(ErrorCode.WISH_ALREADY_EXISTS);
                });

        wishesRepository.save(createWish(memberId, productId));
    }

    public void removeWishIfPresent(Long memberId, Long productId) {
        wishesRepository.findByMember_IdAndProduct_Id(memberId, productId)
                .ifPresent(wish -> {
                    wishesRepository.deleteById(wish.getId());
                });
    }

    public Page<ProductResponse> getWishlistOfMember(Long memberId, Pageable pageable) {
        return wishesRepository.findByMember_Id(memberId, pageable)
                .map(Wish::getProduct)
                .map(ProductMapper::toResponseDto);
    }

    private Wish createWish(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

        return new Wish(member, product);
    }

}
