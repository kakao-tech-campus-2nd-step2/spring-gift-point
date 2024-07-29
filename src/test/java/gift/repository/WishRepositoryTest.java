package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import gift.domain.category.entity.Category;
import gift.domain.category.repository.CategoryRepository;
import gift.domain.member.entity.Member;
import gift.domain.member.repository.MemberRepository;
import gift.domain.product.entity.Product;
import gift.domain.product.repository.ProductRepository;
import gift.domain.wishlist.entity.Wish;
import gift.domain.wishlist.repository.WishRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;

@DataJpaTest
@TestPropertySource(properties = {
    "spring.sql.init.mode=never"
})
class WishRepositoryTest {

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Member로 wish 리스트 가져오는 findAllByMember 테스트")
    void findAllByMemberTest() {
        // given
        Member savedMember = memberRepository.save(createMember());

        Category savedCategory = categoryRepository.save(createCategory());
        Product savedProduct1 = productRepository.save(createProduct(savedCategory));
        Product savedProduct2 = productRepository.save(createProduct(savedCategory));

        Wish expected1 = wishRepository.save(new Wish(savedMember, savedProduct1));
        Wish expected2 = wishRepository.save(new Wish(savedMember, savedProduct2));

        Pageable pageable = PageRequest.of(0, 10);

        // when
        Page<Wish> actual = wishRepository.findAllByMember(savedMember, pageable);

        // then
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(2);
        assertThat(actual).containsExactlyInAnyOrder(expected1, expected2);
    }

    @Test
    @DisplayName("findById 테스트")
    void findByIdTest() {
        // given
        Member savedMember = memberRepository.save(createMember());

        Category savedCategory = categoryRepository.save(createCategory());
        Product savedProduct = productRepository.save(createProduct(savedCategory));

        Wish expected = wishRepository.save(new Wish(savedMember, savedProduct));

        // when
        Wish actual = wishRepository.findById(expected.getId()).orElseThrow();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("save 테스트")
    void saveTest() {
        // given
        Member savedMember = memberRepository.save(createMember());

        Category savedCategory = categoryRepository.save(createCategory());
        Product savedProduct = productRepository.save(createProduct(savedCategory));

        Wish expected = new Wish(savedMember, savedProduct);

        // when
        Wish actual = wishRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getMember()).isEqualTo(expected.getMember()),
            () -> assertThat(actual.getProduct()).isEqualTo(expected.getProduct())
        );
    }

    @Test
    @DisplayName("delete 테스트")
    void deleteTest() {
        // given-
        Member savedMember = memberRepository.save(createMember());

        Category savedCategory = categoryRepository.save(createCategory());
        Product savedProduct = productRepository.save(createProduct(savedCategory));

        Wish savedWish = wishRepository.save(new Wish(savedMember, savedProduct));

        // when
        wishRepository.delete(savedWish);

        // then
        assertTrue(wishRepository.findById(savedWish.getId()).isEmpty());
    }

    Member createMember() {
        return new Member("test", "password");
    }

    Category createCategory() {
        return new Category("test", "color", "image", "description");
    }

    Product createProduct(Category category) {
        return new Product("product", 1000, "product1.jpg", category);
    }
}