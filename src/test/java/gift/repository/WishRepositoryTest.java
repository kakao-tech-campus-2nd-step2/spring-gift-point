package gift.repository;

import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.Wish;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class WishRepositoryTest {
    /*
    @Autowired
    private WishRepository wishes;
    @Autowired
    private ProductRepository products;
    @Autowired
    private MemberRepository members;
    @Autowired
    private CategoryRepository categories;

    @Test
    @DisplayName("위시 저장 테스트")
    void save() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();
        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();

        categories.save(category);
        products.save(expectedProduct);

        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        members.save(expectedMember);

        Wish expected = new Wish(expectedMember,expectedProduct,10);

        // when
        Wish actual = wishes.save(expected);

        // then
        assertAll(
                ()->assertThat(actual.getId()).isNotNull(),
                ()->assertThat(actual.getProduct()).isEqualTo(expected.getProduct()),
                ()->assertThat(actual.getMember()).isEqualTo(expected.getMember()),
                ()->assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("위시 멤버 아이디로 위시 조회 테스트")
    void findByMemberId() {
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        products.save(expectedProduct);

        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        members.save(expectedMember);

        Wish expected = new Wish(expectedMember,expectedProduct,10);
        wishes.save(expected);

        // when
        Wish foundWish = wishes.findById(expected.getId()).orElse(null);

        // then
        assertNotNull(foundWish);
        assertAll(
                () -> assertThat(foundWish.getId()).isNotNull(),
                () -> assertThat(foundWish.getMember().getEmail()).isEqualTo(expected.getMember().getEmail()),
                () -> assertThat(foundWish.getProduct().getName()).isEqualTo(expected.getProduct().getName()),
                () -> assertThat(foundWish.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("위시 아이디와 멤버 아이디로 위시 조회 테스트")
    void findByIdAndMemberId() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        products.save(expectedProduct);

        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        members.save(expectedMember);

        Wish expected = new Wish(expectedMember,expectedProduct,10);
        wishes.save(expected);

        // when
        Wish foundWish = wishes.findByIdAndMemberId(expected.getId(),expected.getMember().getId()).orElse(null);

        // then
        assertNotNull(foundWish);
        assertAll(
                () -> assertThat(foundWish.getId()).isNotNull(),
                () -> assertThat(foundWish.getMember()).isEqualTo(expected.getMember()),
                () -> assertThat(foundWish.getProduct()).isEqualTo(expected.getProduct()),
                () -> assertThat(foundWish.getQuantity()).isEqualTo(expected.getQuantity())
        );
    }

    @Test
    @DisplayName("위시 아이디로 위시 삭제 테스트")
    void deleteById() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        products.save(expectedProduct);

        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        members.save(expectedMember);

        Wish expected = new Wish(expectedMember,expectedProduct,10);
        wishes.save(expected);

        // when
        expected.remove();
        wishes.deleteById(expected.getId());


        // then
        List<Wish> findWishes = wishes.findAll();
        assertAll(
                () -> assertThat(findWishes.size()).isEqualTo(0)
        );
    }

    @Test
    @DisplayName("위시 페이지 조회 테스트")
    void testFindByMemberId() {
        // given
        Category category = new Category.Builder()
                .name("교환권")
                .color("#FF5733")
                .imageUrl("https://example.com/images/exchange_voucher.jpg")
                .description("다양한 상품으로 교환 가능한 교환권")
                .build();

        Product expectedProduct = new Product.Builder()
                .name("아메리카노")
                .price(2000)
                .imageUrl("https://example.com/americano")
                .category(category)
                .build();
        categories.save(category);
        products.save(expectedProduct);

        Member expectedMember = new Member.Builder()
                .email("a@a.com")
                .password("1234")
                .build();
        members.save(expectedMember);

        wishes.save(new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(1)
                .build());

        wishes.save(new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(2)
                .build());

        wishes.save(new Wish.Builder()
                .member(expectedMember)
                .product(expectedProduct)
                .qunatity(3)
                .build());

        // when
        Pageable pageable = PageRequest.of(0,2, Sort.by("id").descending());
        Page<Wish> page = wishes.findByMemberId(expectedMember.getId(), pageable);

        // then
        assertAll(
                () -> assertThat(page.getContent().size()).isEqualTo(2),
                () -> assertThat(page.getTotalElements()).isEqualTo(3),
                () -> assertThat(page.getTotalPages()).isEqualTo(2)
        );
    }
    */
}