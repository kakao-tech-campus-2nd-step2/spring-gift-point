package gift.service;

import gift.repository.WishlistRepository;
import gift.vo.Member;
import gift.vo.Product;
import gift.vo.Wish;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final MemberService memberService;
    private final ProductService productService;

    public WishlistService(WishlistRepository wishlistRepository, MemberService memberService, ProductService productService) {
        this.wishlistRepository = wishlistRepository;
        this.memberService = memberService;
        this.productService = productService;
    }

    private Member getMember(Long memberId) {
        return memberService.getMemberById(memberId);
    }

    private Product getProduct(Long productId) {
        return productService.getProductById(productId);
    }

    public Page<Wish> getWishProductList(Long memberId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return wishlistRepository.findByMemberId(memberId, pageable);
    }

    public void addWishProduct(Long memberId, Long productId) {
        Wish wish = new Wish(getMember(memberId), getProduct(productId));
        wishlistRepository.save(wish);
    }

    public void deleteWishProduct(Long id) {
        wishlistRepository.deleteById(id);
    }

    public Long hasFindWishByMemberAndProduct(Member member, Product product) {
        return wishlistRepository.findByMemberAndProduct(member, product)
                .map(Wish::getId)
                .orElse(null);
    }
}
