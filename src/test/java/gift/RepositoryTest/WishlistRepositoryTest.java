package gift.RepositoryTest;

import gift.Entity.*;
import gift.Repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class WishlistRepositoryTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private WishlistJpaRepository wishlistJpaRepository;

    @Autowired
    private MemberJpaRepository memberJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    private Member member;
    private Category category;
    private Product product;
    private Option option;

    @BeforeEach
    void setUp() {

        member = new Member(1L, "1234@naver.com", "1234", false);
        memberJpaRepository.save(member);

        category = new Category(1L, "category1", "식품", "###", "http://localhost:8080/image1.jpg");
        categoryJpaRepository.save(category);

        product = new Product(1L, "productDto1", category, 1000, "http://localhost:8080/image1.jpg");
        productJpaRepository.save(product);

        option = new Option(1L, product, "option1", 1000);
        optionJpaRepository.save(option);

    }

    @Test
    public void testGetWishlist() {
        Wishlist wishlist = new Wishlist(1L, member, product, "test", 1, 1000, option);
        Wishlist savedwishlist = wishlistJpaRepository.save(wishlist);

        assertThat(savedwishlist.getMember().getId()).isEqualTo(wishlist.getMember().getId());
        assertThat(savedwishlist.getProduct().getId()).isEqualTo(wishlist.getProduct().getId());
        assertThat(savedwishlist.getCount()).isEqualTo(wishlist.getCount());
        assertThat(savedwishlist.getPrice()).isEqualTo(wishlist.getPrice());
        assertThat(savedwishlist.getProductName()).isEqualTo(wishlist.getProductName());
    }

    @Test
    public void testAddWishlist() {
        Wishlist wishlist = new Wishlist(1L, member, product, "test", 1, 1000, option);
        Wishlist savedwishlist = wishlistJpaRepository.save(wishlist);

        assertThat(savedwishlist.getMember().getId()).isEqualTo(wishlist.getMember().getId());
        assertThat(savedwishlist.getProduct().getId()).isEqualTo(wishlist.getProduct().getId());
        assertThat(savedwishlist.getCount()).isEqualTo(wishlist.getCount());
        assertThat(savedwishlist.getPrice()).isEqualTo(wishlist.getPrice());
        assertThat(savedwishlist.getProductName()).isEqualTo(wishlist.getProductName());
    }

    @Test
    @DisplayName("delete가 정상적으로 이루어지는지")
    public void testRemoveWishlist() {
        Wishlist wishlist = new Wishlist(1L, member, product, "test", 1, 1000, option);
        Wishlist savedwishlist = wishlistJpaRepository.save(wishlist);

        wishlistJpaRepository.delete(savedwishlist);

        Optional<Wishlist> foundWishlist = wishlistJpaRepository.findById(savedwishlist.getId());

        assertThat(foundWishlist).isEmpty();

    }

    @Test
    @DisplayName("Update가 정상적으로 이루어지는지")
    public void testUpdateWishlistItem() {
        Wishlist wishlist = new Wishlist(1L, member, product, "test", 5, 5000, option);
        wishlistJpaRepository.save(wishlist);

        //수량을 5개에서 3개로 변경
        Wishlist updateWishlist = new Wishlist(1L, member, product, "test", 3, 3000, option);
        wishlistJpaRepository.save(updateWishlist);

        Optional<Wishlist> foundWishlistOptional = wishlistJpaRepository.findById(updateWishlist.getId());
        Wishlist foundWishlist = foundWishlistOptional.get();
        assertThat(foundWishlist.getMember().getId()).isEqualTo(updateWishlist.getMember().getId());
        assertThat(foundWishlist.getProduct().getId()).isEqualTo(updateWishlist.getProduct().getId());
        assertThat(foundWishlist.getCount()).isEqualTo(updateWishlist.getCount());
        assertThat(foundWishlist.getProductName()).isEqualTo(updateWishlist.getProductName());
        assertThat(foundWishlist.getPrice()).isEqualTo(updateWishlist.getPrice());

    }
}
