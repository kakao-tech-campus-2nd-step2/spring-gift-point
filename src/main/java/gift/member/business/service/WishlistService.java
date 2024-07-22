package gift.member.business.service;

import gift.member.business.dto.WishlistPagingDto;
import gift.member.persistence.entity.Wishlist;
import gift.member.persistence.repository.MemberRepository;
import gift.member.persistence.repository.WishlistRepository;
import gift.member.business.dto.WishlistUpdateDto;
import gift.product.persistence.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishlistService(WishlistRepository wishlistRepository,
        ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public WishlistPagingDto getWishListsByPage(Long memberId, Pageable pageable) {
        Page<Wishlist> wishlists = wishlistRepository.getWishListByPage(memberId, pageable);
        return WishlistPagingDto.from(wishlists);
    }

    public Long addWishList(Long memberId, Long productId) {
        var product = productRepository.getReferencedProduct(productId);
        var member = memberRepository.getReferencedMember(memberId);

        var wishList = new Wishlist(product, member, 1);
        return wishlistRepository.saveWishList(wishList);
    }

    public Long updateWishList(Long memberId, WishlistUpdateDto wishListUpdateDto) {
        var wishList = wishlistRepository.getWishListByMemberIdAndProductId(
            memberId, wishListUpdateDto.productId());

        wishList.setCount(wishListUpdateDto.count());
        return wishlistRepository.updateWishlist(wishList);
    }

    @Transactional
    public void deleteWishList(Long memberId, Long productId) {
        wishlistRepository.deleteWishlist(memberId, productId);
    }

}
