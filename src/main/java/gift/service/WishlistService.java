package gift.service;

import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.WishlistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository, MemberService memberService,
        ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    public Page<Product> getWishlist(String email, Pageable pageable) {
        Member member = memberService.findMemberByEmail(email);
        Page<Wishlist> wishlistItems = wishlistRepository.findByMember(member, pageable);
        return wishlistItems.map(Wishlist::getProduct);
    }

    @Transactional
    public void addWishlist(String email, Long productId) {
        Member member = memberService.findMemberByEmail(email);
        Product product = productService.findProductsById(productId);
        Wishlist wishlist = new Wishlist(null, member, product);
        wishlistRepository.save(wishlist);
    }

    @Transactional
    public void removeWishlist(String email, Long productId) {
        Member member = memberService.findMemberByEmail(email);
        Product product = productService.findProductsById(productId);
        wishlistRepository.deleteByMemberAndProduct(member, product);
    }
}