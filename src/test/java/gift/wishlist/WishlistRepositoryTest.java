package gift.wishlist;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.category.entity.Category;
import gift.category.CategoryRepository;
import gift.member.Member;
import gift.member.MemberRepository;
import gift.product.entity.Product;
import gift.product.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.LongStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setupMemberAndProduct() {
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY FALSE");
        jdbcTemplate.execute("TRUNCATE TABLE product RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE member");
        jdbcTemplate.execute("TRUNCATE TABLE wishlist RESTART IDENTITY");
        jdbcTemplate.execute("TRUNCATE TABLE category RESTART IDENTITY");
        jdbcTemplate.execute("SET REFERENTIAL_INTEGRITY TRUE");

        memberRepository.save(new Member("aaa@email.com", "password"));
        memberRepository.save(new Member("bbb@email.com", "password"));

        for (int i = 1; i < 4; i++) {
            categoryRepository.save(new Category(i, "category" + i));
            productRepository.save(
                new Product(i, "product-" + i, i * 100, "image-url-" + i,
                    new Category(1L, "category-1")));
        }
    }

    @Test
    @DisplayName("[Unit] getAllWishlists test")
    void getAllWishlistsTest() {
        //given
        Member member = memberRepository.findById("aaa@email.com").get();
        List<Wishlist> expect = productRepository.findAllById(
                LongStream.range(1L, 4L)
                    .boxed()
                    .toList()
            ).stream()
            .map(e -> new Wishlist(e, member))
            .toList();

        wishlistRepository.saveAll(expect);

        Product product = productRepository.save(
            new Product(4L, "product-4", 400, "product-4-image", new Category(1L, "category-1"))
        );

        wishlistRepository.save(
            new Wishlist(product, memberRepository.findById("bbb@email.com").get())
        );

        //when
        List<Wishlist> actual = wishlistRepository.findAllByMember(member);

        //then
        assertAll(
            () -> assertThat(actual).hasSize(expect.size()),
            () -> assertThat(actual.get(0)).isEqualTo(expect.get(0)),
            () -> assertThat(actual.get(1)).isEqualTo(expect.get(1)),
            () -> assertThat(actual.get(2)).isEqualTo(expect.get(2))
        );
    }

    @Test
    @DisplayName("[Unit] addWishlist test")
    void addWishlistTest() {
        //given
        Wishlist expect = new Wishlist(
            productRepository.findById(1L).get(),
            memberRepository.findById("aaa@email.com").get()
        );

        //when
        Wishlist actual = wishlistRepository.save(expect);

        //then
        assertThat(actual).isEqualTo(expect);
    }

    @Test
    @DisplayName("[Unit] deleteWishlist test")
    void deleteWishlistTest() {
        //given
        Wishlist expect = wishlistRepository.save(
            new Wishlist(
                productRepository.findById(1L).get(),
                memberRepository.findById("aaa@email.com").get()
            )
        );

        //when
        wishlistRepository.delete(expect);
        Optional<Wishlist> actual = wishlistRepository.findById(1L);

        //then
        assertThat(actual).isEmpty();
    }
}