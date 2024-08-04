package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.TokenAuth;
import gift.domain.WishlistItem;
import gift.dto.request.WishlistRequest;
import gift.dto.response.WishlistPageResponse;
import gift.exception.MemberNotFoundException;
import gift.exception.ProductNotFoundException;
import gift.exception.WishlistNotFoundException;
import gift.repository.member.MemberSpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import gift.repository.wishlist.WishlistSpringDataJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static gift.exception.ErrorCode.*;

@Service
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistSpringDataJpaRepository wishlistRepository;
    private final TokenService tokenService;
    private final ProductSpringDataJpaRepository productRepository;
    private final MemberSpringDataJpaRepository memberRepository;

    @Autowired
    public WishlistService(WishlistSpringDataJpaRepository wishlistRepository, TokenService tokenService, ProductSpringDataJpaRepository productRepository, MemberSpringDataJpaRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.tokenService = tokenService;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void addItemToWishlist(WishlistRequest wishlistRequest, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberNotFoundException(MEMBER_NOT_FOUND));
        Product product = productRepository.findById(wishlistRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        WishlistItem item = new WishlistItem(member, product);
        wishlistRepository.save(item);
    }

    @Transactional
    public void deleteItemFromWishlist(Long productId, Long memberId) {
        WishlistItem item = wishlistRepository.findByMemberIdAndProductId(memberId, productId)
                .orElseThrow(() -> new WishlistNotFoundException(WISHLIST_NOT_FOUND));

        wishlistRepository.delete(item);
    }

    public List<WishlistItem> getWishlistByMemberId(Long memberId) {
        return wishlistRepository.findByMemberId(memberId);
    }

    public WishlistPageResponse getWishlistByMemberId(Long memberId, Pageable pageable) {
        Page<WishlistItem> wishlist = wishlistRepository.findByMemberId(memberId, pageable);
        return WishlistPageResponse.fromWishlistPage(wishlist);
    }


}
