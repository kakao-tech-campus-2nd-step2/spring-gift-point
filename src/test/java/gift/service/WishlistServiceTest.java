package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.dto.pageDTO.WishlistPageResponseDTO;
import gift.dto.wishlistDTO.WishlistRequestDTO;
import gift.dto.wishlistDTO.WishlistResponseDTO;
import gift.model.Category;
import gift.model.Member;
import gift.model.Option;
import gift.model.Product;
import gift.model.Wishlist;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

    @Autowired
    private OptionRepository optionRepository;

    private Member member;
    private Product product;
    private Category category;
    private Option option;
    private Wishlist wishlist;
    private Pageable pageable = PageRequest.of(0, 5);

    @BeforeEach
    void setUp() {
        category = new Category(null, "임시카테고리", "#770077", "임시 이미지", "임시 설명");
        categoryRepository.save(category);
        member = new Member(null, "kbm@kbm.com", "kbm", "user");
        memberRepository.save(member);
        product = new Product(null, "상품1", "100", category, "https://kakao");
        productRepository.save(product);
        option = new Option(null, "옵션1", 10L, product);
        optionRepository.save(option);
    }

    @Test
    void testGetWishlists() {
        wishlist = new Wishlist(null, member, option);
        wishlistRepository.save(wishlist);
        WishlistPageResponseDTO wishlists = wishlistService.getWishlists(member.getEmail(),
            pageable);
        assertAll(
            () -> assertEquals(1, wishlists.wishlists().getTotalElements()),
            () -> assertEquals("상품1",
                wishlists.wishlists().getContent().get(0).getOption().getProduct().getName())
        );
    }

    @Test
    @Transactional
    void testAddWishlist() {
        WishlistRequestDTO wishlistRequestDTO = new WishlistRequestDTO(option.getId());
        WishlistResponseDTO wishlistResponseDTO = wishlistService.addWishlist(member.getEmail(),
            wishlistRequestDTO);
        List<Wishlist> wishlists = wishlistRepository.findAll();
        Wishlist wishlist = wishlists.get(0);
        assertAll(
            () -> assertEquals(1, wishlists.size()),
            () -> assertEquals(member.getId(), wishlist.getMember().getId()),
            () -> assertEquals(option.getId(), wishlist.getOption().getId()),
            () -> assertEquals(wishlistResponseDTO.productId(), option.getProduct().getId())
        );
    }

    @Test
    @Transactional
    void testRemoveWishlist() {
        wishlist = new Wishlist(null, member, option);
        wishlistRepository.save(wishlist);
        wishlistService.removeWishlist(wishlist.getId());
        List<Wishlist> wishlists = wishlistRepository.findAll();
        assertTrue(wishlists.isEmpty());
    }

    @Test
    @Transactional
    void testRemoveWishlistByOptionId() {
        wishlist = new Wishlist(null, member, option);
        wishlistRepository.save(wishlist);
        wishlistService.removeWishlistByOptionId(option.getId());
        List<Wishlist> wishlists = wishlistRepository.findAll();
        assertTrue(wishlists.isEmpty());
    }
}