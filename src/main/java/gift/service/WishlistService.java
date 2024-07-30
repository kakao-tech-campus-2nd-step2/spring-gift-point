package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.TokenAuth;
import gift.domain.WishlistItem;
import gift.dto.request.WishlistRequest;
import gift.exception.ProductNotFoundException;
import gift.exception.WishlistNotFoundException;
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

    @Autowired
    public WishlistService(WishlistSpringDataJpaRepository wishlistRepository, TokenService tokenService, ProductSpringDataJpaRepository productRepository) {
        this.wishlistRepository = wishlistRepository;
        this.tokenService = tokenService;
        this.productRepository = productRepository;
    }

    @Transactional
    public void addItemToWishlist(WishlistRequest wishlistRequest, String token) {
        TokenAuth tokenAuth = tokenService.findToken(token);
        Member member = tokenAuth.getMember();
        Product product = productRepository.findById(wishlistRequest.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));

        WishlistItem item = new WishlistItem(member, product);
        wishlistRepository.save(item);
    }

    @Transactional
    public void deleteItemFromWishlist(Long productId, String token) {
        TokenAuth tokenAuth = tokenService.findToken(token);
        Member member = tokenAuth.getMember();

        WishlistItem item = wishlistRepository.findByMemberIdAndProductId(member.getId(), productId)
                .orElseThrow(() -> new WishlistNotFoundException(WISHLIST_NOT_FOUND));

        wishlistRepository.delete(item);
    }

    public List<WishlistItem> getWishlistByMemberId(Long memberId) {
        return wishlistRepository.findByMemberId(memberId);
    }

    public Page<WishlistItem> getWishlistByMemberId(Long memberId, Pageable pageable) {
        return wishlistRepository.findByMemberId(memberId, pageable);
    }


}
