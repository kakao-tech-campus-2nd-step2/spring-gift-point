package gift.entity;

import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.ProductRepository;
import gift.repository.WishlistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class WishlistTest {
    @Autowired
    private WishlistRepository wishlistRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Wishlist testWishlist;
    private Member testMember;
    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("test", "test", "test", "test");
        testProduct = new Product(testCategory, 1, "test", "test");
        testMember = new Member("test", "test", "test");
        categoryRepository.save(testCategory);
        productRepository.save(testProduct);
        memberRepository.save(testMember);

        testWishlist = new Wishlist(testMember, testProduct, 1);
    }

    @AfterEach
    public void tearDown() {
        wishlistRepository.deleteAll();
        memberRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    void testAddWishlist() {
        Wishlist savedWishlist = wishlistRepository.save(testWishlist);
        assertNotNull(savedWishlist.getId());
        assertEquals(1, wishlistRepository.findAll().size());
    }

    @Test
    void testFindWishlistById() {
        Wishlist savedWishlist = wishlistRepository.save(testWishlist);
        Wishlist foundWishlist = wishlistRepository.findById(savedWishlist.getId()).get();
        assertNotNull(foundWishlist);
        assertEquals(testMember.getId(), foundWishlist.getMember().getId());
        assertEquals(testProduct.getId(), foundWishlist.getProduct().getId());
        assertEquals(1, foundWishlist.getCountProduct());
    }

    @Test
    void testUpdateWishlist() {
        Wishlist savedWishlist = wishlistRepository.save(testWishlist);
        savedWishlist.setCountProduct(2);
        Wishlist updatedWishlist = wishlistRepository.save(savedWishlist);
        assertEquals(2, updatedWishlist.getCountProduct());
    }


    @Test
    void testDeleteWishlist() {
        Wishlist savedWishlist = wishlistRepository.save(testWishlist);
        wishlistRepository.delete(savedWishlist);
        assertTrue(wishlistRepository.findById(savedWishlist.getId()).isEmpty());
    }

    @Test
    void testUniqueConstraint() {
        wishlistRepository.saveAndFlush(testWishlist);

        Member newMember = new Member("newEmail", "newPassword", "newToken");
        memberRepository.saveAndFlush(newMember);

        Wishlist duplicateWishlist = new Wishlist(newMember, testProduct, 2);
        assertDoesNotThrow(() -> wishlistRepository.saveAndFlush(duplicateWishlist));
    }
}