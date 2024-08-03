package gift.service;

import gift.dto.response.WishlistResponse;
import gift.entity.Member;

import gift.entity.Product;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final MemberService memberService;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, ProductService productService,
                           MemberService memberService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
        this.memberService = memberService;
    }

    public boolean isProductInWishlist(Long memberId, Long productId) {
        // Wishlist 테이블에서 userId와 productId로 조회하여 존재 여부 반환
        return wishlistRepository.existsByMemberIdAndProductId(memberId, productId);
    }

    public Page<Product> getWishlistByEmail(String email, Pageable pageable) {
        Long memberId = memberService.getMember(email).getId();
        Page<Wishlist> wishlist = wishlistRepository.findByMemberId(memberId, pageable);
        return wishlist.map(Wishlist::getProduct);
    }

    public Page<WishlistResponse> getWishlistByMemberId(Long memberId, Pageable pageable) {
        Page<Wishlist> wishlist = wishlistRepository.findByMemberId(memberId, pageable);
        return wishlist.map(WishlistResponse::new);
    }

    public void deleteWishlistItem(String email, Long productId) {
        Long memberId = memberService.getMember(email).getId();
        Wishlist wish = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
        if (wish != null) {
            wishlistRepository.delete(wish);
        }
    }
    //WishlistNewController에서 사용
    public void deleteWishlistItemById(Long wishId) {
        Optional<Wishlist> wish = wishlistRepository.findById(wishId);
        if (wish.isPresent()) {
            wishlistRepository.delete(wish.get());
        }
    }

    public void addWishlistItem(String email, Long productId) {
        Member member = memberService.getMember(email);
        Product product = productService.getProductById(productId);
        wishlistRepository.save(new Wishlist(member, product));
    }
}
