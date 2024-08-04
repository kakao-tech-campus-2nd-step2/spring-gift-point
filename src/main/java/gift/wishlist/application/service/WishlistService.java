package gift.wishlist.application.service;

import gift.exception.InvalidProduct;
import gift.member.persistence.Member;
import gift.member.persistence.MemberRepository;
import gift.product.persistence.Product;
import gift.product.persistence.ProductRepository;
import gift.wishlist.application.dto.ProductDto;
import gift.wishlist.application.dto.WishPageDto;
import gift.wishlist.application.dto.WishRequestDto;
import gift.wishlist.application.dto.WishResponseDto;
import gift.wishlist.persistence.WishlistRepository;
import gift.wishlist.persistence.Wishlist;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    public WishlistService(
        WishlistRepository wishlistRepository,
        ProductRepository productRepository,
        MemberRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }

    public Page<WishPageDto> checkWishlist(Member member, Pageable pageable) {
        Page<Wishlist> wishlistPage = wishlistRepository.findAllByMemberId(member.getId(), pageable);
        return wishlistPage.map(this::convertToWishlistDto);
    }

    private WishPageDto convertToWishlistDto(Wishlist wishlist) {
        ProductDto productDto = new ProductDto(
            wishlist.getProduct().getId(),
            wishlist.getProduct().getName(),
            wishlist.getProduct().getPrice(),
            wishlist.getProduct().getImageUrl(),
            wishlist.getProduct().getCategory().getId()
        );

        return new WishPageDto(wishlist.getId(), productDto);
    }

    @Transactional
    public WishResponseDto addWishlist(WishRequestDto request, Member member) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new InvalidProduct("유효하지 않은 상품입니다"));

        Wishlist wishlist = new Wishlist(member, product, request.quantity());

        wishlistRepository.saveAndFlush(wishlist);

        return new WishResponseDto(wishlist.getId(), wishlist.getQuantity());
    }

    @Transactional
    public void deleteWishlist(Long productId, Long memberId) {
        List<Wishlist> wishlist1 = wishlistRepository.findByProductId(productId);
        List<Wishlist> wishlist2 = wishlistRepository.findByMemberId(memberId);

        Wishlist wishlistItem = null;
        for (Wishlist item : wishlist1) {
            if (wishlist2.contains(item)) {
                wishlistItem = item;
                break;
            }
        }

        if (wishlistItem != null) {
            wishlistRepository.delete(wishlistItem);
        } else {
            throw new InvalidProduct("잘못된 접근입니다");
        }
    }
}
