package gift.service;

import gift.DTO.product.ProductResponse;
import gift.DTO.wishlist.WishResponse;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wishlist;
import gift.exception.product.ProductNotFoundException;
import gift.exception.wish.WishNotFoundException;
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
    public Page<WishResponse> getWishlistByEmail(String email, Pageable pageable) {
        Page<Wishlist> wishes = wishlistRepository.findByMemberEmail(email, pageable);
        return wishes.map(WishResponse::fromEntity);
    }

    @Transactional
    public WishResponse addWishlist(String email, Long productId) {
        Product product= productRepository.findById(productId)
                                        .orElseThrow(() -> new ProductNotFoundException());
        Member member = memberService.getMemberByEmail(email);

        Wishlist wish = new Wishlist(member, product);
        Wishlist savedWish = wishlistRepository.save(wish);
        return WishResponse.fromEntity(savedWish);
    }

    @Transactional
    public void removeWishlist(String email, Long productId) {
        Wishlist wish = wishlistRepository.findByMemberEmailAndProductId(email, productId)
            .orElseThrow(() -> new WishNotFoundException());
        wishlistRepository.delete(wish);
    }

    @Transactional
    public void deleteWish(String email, Long wishId) {
        Wishlist wish = wishlistRepository.findById(wishId)
            .orElseThrow(() -> new WishNotFoundException());
        wishlistRepository.delete(wish);
    }
}
