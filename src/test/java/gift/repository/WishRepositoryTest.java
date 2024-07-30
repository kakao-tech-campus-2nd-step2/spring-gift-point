package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.category.CategoryRepository;
import gift.category.model.Category;
import gift.member.MemberRepository;
import gift.member.model.Member;
import gift.product.ProductRepository;
import gift.product.model.Product;
import gift.wish.WishRepository;
import gift.wish.model.Wish;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class WishRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WishRepository wishRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category expectedCategory;
    private Member expectedMember;
    private Product expectedProduct1;
    private Product expectedProduct2;

    @BeforeEach
    void setUp() throws Exception {
        saveCategory();
        saveMember();
        saveProduct();
    }

    @Test
    void save() {
        Wish expected = createWish(expectedMember, expectedProduct1);

        Wish actual = wishRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getMember().getId()).isEqualTo(expectedMember.getId()),
            () -> assertThat(actual.getProduct().getId()).isEqualTo(expectedProduct1.getId()),
            () -> assertThat(actual.getCount()).isEqualTo(expected.getCount())
        );
    }

    @Test
    void findAllByMemberId() {
        Wish expected1 = createWish(expectedMember, expectedProduct1);
        Wish expected2 = createWish(expectedMember, expectedProduct2);
        wishRepository.save(expected1);
        wishRepository.save(expected2);

        List<Wish> actual = wishRepository.findAllByMemberId(expectedMember.getId(),
            PageRequest.of(0, 10, Sort.by(
                Direction.ASC, "product"))).getContent();

        assertThat(actual).containsExactly(expected1, expected2);
    }

    @Test
    void findByMemberIdAndProductId() {
        Wish expected = createWish(expectedMember, expectedProduct1);

        wishRepository.save(expected);

        Wish actual = wishRepository.findByMemberIdAndProductId(expectedMember.getId(),
            expectedProduct1.getId()).orElseThrow();

        assertAll(
            () -> assertThat(actual.getMember().getId()).isEqualTo(expectedMember.getId()),
            () -> assertThat(actual.getMember().getRole()).isEqualTo(expectedMember.getRole()),
            () -> assertThat(actual.getProduct().getId()).isEqualTo(expectedProduct1.getId()),
            () -> assertThat(actual.getProduct().getPrice()).isEqualTo(expectedProduct1.getPrice()),
            () -> assertThat(actual.getCount()).isEqualTo(expected.getCount())
        );
    }

    @Test
    void deleteByMemberIdAndProductId() {
        Wish expected = createWish(expectedMember, expectedProduct1);
        wishRepository.save(expected);

        wishRepository.deleteByMemberIdAndProductId(expected.getMember().getId(),
            expected.getProduct().getId());

        Optional<Wish> actual = wishRepository.findByMemberIdAndProductId(expectedMember.getId(),
            expectedProduct1.getId());

        assertThat(actual).isEmpty();
    }

    private Wish createWish(Member expectedMember, Product expectedProduct) {
        return new Wish(
            expectedMember, expectedProduct, 1);
    }

    private void saveProduct() {
        if (productRepository.findAll().isEmpty()) {
            expectedProduct1 = new Product("gamza", 500, "gamza.jpg", expectedCategory);
            expectedProduct2 = new Product("goguma", 1500, "goguma.jpg", expectedCategory);
            productRepository.save(expectedProduct1);
            productRepository.save(expectedProduct2);
        }
    }

    private void saveMember() {
        if (memberRepository.findAll().isEmpty()) {
            expectedMember = new Member("member1@example.com", "password1", "member1", "user");
            memberRepository.save(expectedMember);
        }
    }

    private void saveCategory() {
        if (categoryRepository.findAll().isEmpty()) {
            expectedCategory = new Category("test", "##test", "test.jpg", "test");
            categoryRepository.save(expectedCategory);
        }
    }
}
