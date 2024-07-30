package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.category.CategoryRepository;
import gift.category.Category;
import gift.member.Member;
import gift.member.MemberRepository;
import gift.product.Product;
import gift.product.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WishlistRepositoryTest {

    @Autowired
    private WishlistRepository wishlistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setCategoryRepository() {
        categoryRepository.save(new Category("교환권","쌈@뽕한 블루","www","여름"));
        categoryRepository.save(new Category("과제면제권","방학","www.com","학교"));
        categoryRepository.save(new Category("라우브","스틸더","www.show","키야"));
    }

    @BeforeEach
    void setProductRepository() {
        productRepository.save(new Product("사과", 2000, "www", categoryRepository.findById(1L).get()));
        productRepository.save(new Product("바나나", 3000,"www.com", categoryRepository.findById(2L).get()));
    }

    @BeforeEach
    void setMemberRepository() {
        memberRepository.save(new Member("westzeroright","errorai"));
        memberRepository.save(new Member("econo","404"));
    }

    @Test
    @DisplayName("장바구니 추가 테스트")
    void add() {
        Wishlist expected = new Wishlist(
            memberRepository.findById(1L).get(),
            productRepository.findById(1L).get()
        );

        Wishlist actual = wishlistRepository.save(expected);
        assertAll(
            () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    @DisplayName("장바구니 삭제 테스트")
    void delete() {
        Wishlist expected = new Wishlist(
            memberRepository.findById(1L).get(),
            productRepository.findById(1L).get()
        );
        wishlistRepository.delete(expected);

        List<Wishlist> isWishlist = wishlistRepository.findById(expected.getProduct().getId()).stream().toList();

        assertAll(
            () -> assertThat(isWishlist.size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("단일 장바구니 조회 테스트")
    void find() {
        Wishlist expected = new Wishlist(
            memberRepository.findById(1L).get(),
            productRepository.findById(1L).get()
        );
        wishlistRepository.save(expected);

        Wishlist actual = wishlistRepository.findById(expected.getProduct().getId()).get();

        assertAll(
            () -> assertThat(actual.getProduct()).isNotNull(),
            () -> assertThat(actual.getMember()).isNotNull(),
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getProduct().getId()).isEqualTo(1L),
            () -> assertThat(actual.getMember().getId()).isEqualTo(1L)
        );
    }

    @Test
    @DisplayName("모든 장바구니 조회 테스트")
    void findAll() {
        Wishlist expected1 = new Wishlist(
            memberRepository.findById(1L).get(),
            productRepository.findById(1L).get()
        );
        wishlistRepository.save(expected1);

        Wishlist expected2 = new Wishlist(
            memberRepository.findById(2L).get(),
            productRepository.findById(2L).get()
        );
        wishlistRepository.save(expected2);

        List<Wishlist> wishlist = wishlistRepository.findAll();

        assertAll(
            () -> assertThat(wishlist.size()).isEqualTo(2)
        );
    }

}
