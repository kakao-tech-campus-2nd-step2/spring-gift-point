package gift.service;

import gift.DTO.product.ProductResponse;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wishlist;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberService memberService;
    private final CategoryService categoryService;

    @Autowired
    public WishlistService(
        WishlistRepository wr,
        ProductRepository pr,
        MemberService ms,
        CategoryService cs
    ) {
        wishlistRepository = wr;
        productRepository = pr;
        memberService = ms;
        categoryService = cs;
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getWishlistByEmail(String email, Integer pageNumber, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Wishlist> wishes = wishlistRepository.findByMemberEmail(email, pageable);
        return wishes.stream()
                    .map(Wishlist::getProduct)
                    .map(ProductResponse::fromEntity)
                    .toList();
    }

    @Transactional
    public void addWishlist(String email, Long productId) {
        Product product= productRepository.findById(productId)
                                        .orElseThrow(() -> new RuntimeException());
        Member member = memberService.getMemberByEmail(email);

        Wishlist wish = new Wishlist(member, product);
        wishlistRepository.save(wish);
    }

    @Transactional
    public void removeWishlist(String email, Long productId) {
        Wishlist wish = wishlistRepository.findByMemberEmailAndProductId(email, productId)
            .orElseThrow(() -> new RuntimeException("Wish Not Found"));
        wishlistRepository.delete(wish);
    }
}
