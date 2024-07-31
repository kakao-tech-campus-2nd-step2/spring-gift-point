package gift.service;

import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import gift.dto.WishListDto;
import gift.repository.WishRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class WishService{
    private final WishRepository wishRepository;
    private final ProductService productService;
    private final MemberService memberService;
    public WishService(WishRepository wishRepository, ProductService productService, MemberService memberService) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.memberService = memberService;
    }

    public Page<WishListDto> getWishPage(String access_token, Pageable pageable) {
        Member member = memberService.getMember(access_token);
        Page<Wish> wishes = wishRepository.findByMember(member, pageable);
        return wishes.map(WishListDto::new);
    }

    public void addWish(String access_token, Long product_id) {
        Member member = memberService.getMember(access_token);
        Product product = productService.getProduct(product_id);
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);
    }

    public void deleteWish(String access_token, Long product_id) {
        Member member = memberService.getMember(access_token);
        Product product = productService.getProduct(product_id);
        Wish wish = wishRepository.findByProductAndMember(product, member);
        wishRepository.delete(wish);
    }
}
