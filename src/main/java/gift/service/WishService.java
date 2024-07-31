package gift.service;

import gift.config.JwtConfig;
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
    private final JwtConfig jwtConfig;
    public WishService(WishRepository wishRepository, ProductService productService, MemberService memberService, JwtConfig jwtConfig) {
        this.wishRepository = wishRepository;
        this.productService = productService;
        this.memberService = memberService;
        this.jwtConfig = jwtConfig;
    }

    public Page<WishListDto> getWishPage(String access_token, Pageable pageable) {
        String email = jwtConfig.extractEmail(access_token);
        Member member = memberService.getMemberbyEmail(email);
        Page<Wish> wishes = wishRepository.findByMember(member, pageable);
        return wishes.map(WishListDto::new);
    }

    public void addWish(String access_token, Long product_id) {
        String email = jwtConfig.extractEmail(access_token);
        Member member = memberService.getMemberbyEmail(email);
        Product product = productService.getProduct(product_id);
        Wish wish = new Wish(member, product);
        wishRepository.save(wish);
    }

    public void deleteWish(String access_token, Long product_id) {
        String email = jwtConfig.extractEmail(access_token);
        Member member = memberService.getMemberbyEmail(email);
        Product product = productService.getProduct(product_id);
        Wish wish = wishRepository.findByProductAndMember(product, member);
        wishRepository.delete(wish);
    }
}
