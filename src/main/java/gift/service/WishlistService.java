package gift.service;

import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wishlist;
import gift.repository.WishlistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductService productService;
    private final MemberService memberService;
    private final OptionService optionService;

    @Autowired
    public WishlistService(WishlistRepository wishlistRepository, ProductService productService,
                           MemberService memberService, OptionService optionService) {
        this.wishlistRepository = wishlistRepository;
        this.productService = productService;
        this.memberService = memberService;
        this.optionService = optionService;
    }

    public Page<Product> getWishlistByEmail(String email, Pageable pageable) {
        Long memberId = memberService.getMember(email).getId();
        Page<Wishlist> wishlist = wishlistRepository.findByMemberId(memberId, pageable);
        return wishlist.map(Wishlist::getProduct);
    }

    public void deleteWishlistItem(String email, Long productId) {
        Long memberId = memberService.getMember(email).getId();
        Wishlist wish = wishlistRepository.findByMemberIdAndProductId(memberId, productId);
        if (wish != null) {
            wishlistRepository.delete(wish);
        }
    }

    public void addWishlistItem(String email, Long productId) {
        Member member = memberService.getMember(email);
        Product product = productService.getProductById(productId);
        wishlistRepository.save(new Wishlist(member, product));
    }
}
