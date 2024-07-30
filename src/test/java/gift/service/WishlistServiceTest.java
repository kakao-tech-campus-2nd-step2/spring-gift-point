package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.model.Category;
import gift.model.Member;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional(readOnly = true)
class WishlistServiceTest {

    @Autowired
    private WishlistService wishlistService;

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Member member;
    private Product product;
    private Category category;
    private Wishlist wishlist;
    private Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setUp() {
        category = new Category(null, "임시카테고리");
        categoryRepository.save(category);
        member = new Member(null, "kbm", "kbm@kbm.com", "kbm", "user");
        product = new Product(null, "상품1", "100", category, "https://kakao");
        memberRepository.save(member);
        productRepository.save(product);
    }

    @Test
    void testGetWishlist() {
        wishlist = new Wishlist(null, member, product);
        wishlistRepository.save(wishlist);
        Page<Product> wishlists = wishlistService.getWishlist(member.getEmail(), pageable);
        assertAll(
            () -> assertEquals(1, wishlists.getTotalElements()),
            () -> assertEquals("상품1", wishlists.getContent().get(0).getName())
        );
    }

    @Test
    @Transactional
    void testAddWishlist() {
        wishlistService.addWishlist(member.getEmail(), product.getId());
        List<Wishlist> wishlists = wishlistRepository.findAll();
        Wishlist wishlist = wishlists.get(0);
        assertAll(
            () -> assertEquals(1, wishlists.size()),
            () -> assertEquals(member.getId(), wishlist.getMember().getId()),
            () -> assertEquals(product.getId(), wishlist.getProduct().getId())
        );
    }

    @Test
    @Transactional
    void testRemoveWishlist() {
        wishlist = new Wishlist(null, member, product);
        wishlistRepository.save(wishlist);

        wishlistService.removeWishlist(member.getEmail(), product.getId());
        List<Wishlist> wishlists = wishlistRepository.findAll();
        assertTrue(wishlists.isEmpty());
    }
}