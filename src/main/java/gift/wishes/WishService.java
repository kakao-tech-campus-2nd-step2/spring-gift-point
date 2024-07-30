package gift.wishes;


import gift.member.Member;
import gift.member.MemberService;
import gift.product.Product;
import gift.product.ProductService;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishService {

    private final WishRepository wishRepository;
    private final ProductService productService;
    private final MemberService memberService;

    public WishService(WishRepository wishRepository, ProductService productService,
        MemberService memberService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.memberService = memberService;
    }

    public List<WishResponse> findByMemberId(Long memberId) {
        List<Wish> wishList = wishRepository.findByMemberId(memberId);
        return wishList.stream().map(WishResponse::from).toList();
    }

    public WishPageResponse getWishPage(Long memberId, int page) {
        Pageable pageable = PageRequest.of(page, 10);

        return WishPageResponse.from(wishRepository.findByMemberId(memberId, pageable));
    }

    public void createWish(Long memberId, Long productId, Long quantity) {
        Member findMember = memberService.findById(memberId);
        Product findProduct = productService.findById(productId);

        wishRepository.save(new Wish(findMember, findProduct, quantity));
    }

    public void updateQuantity(Long id, Long memberId, Long quantity) {
        if (quantity == 0) {
            deleteWish(id, memberId);
            return;
        }
        Member member = memberService.findById(memberId);
        Wish wish = wishRepository.findByIdAndMember(id, member).orElseThrow();
        wish.updateQuantity(quantity);
    }

    @Transactional
    public void deleteWish(Long id, Long memberId) {
        Member member = memberService.findById(memberId);
        wishRepository.deleteByIdAndMember(id, member);
    }

    public Wish getWish(Long productId, Long memberId){
        return wishRepository.findByProductIdAndMemberId(productId, memberId).orElseThrow();
    }
}
