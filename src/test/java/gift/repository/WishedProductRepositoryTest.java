package gift.repository;

import static gift.util.CategoryFixture.createCategory;
import static gift.util.MemberFixture.createMember;
import static gift.util.ProductFixture.createProduct;
import static gift.util.WishedProductFixture.createWishedProduct;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.domain.Category;
import gift.domain.Member;
import gift.domain.Product;
import gift.domain.WishedProduct;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class WishedProductRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private WishedProductRepository wishedProductRepository;

    private Member member;
    private Category category;
    private Product product;

    @BeforeEach
    void setup() {
        member = createMember();
        member = memberRepository.save(member);
        category = createCategory();
        category = categoryRepository.save(category);
        product = createProduct(category);
        product = productRepository.save(product);
    }

    @DisplayName("위시리스트 상품 추가")
    @Test
    void save() {
        // given
        WishedProduct expected = createWishedProduct(member, product);

        // when
        WishedProduct actual = wishedProductRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getMemberEmail()).isEqualTo(expected.getMemberEmail()),
            () -> assertThat(actual.getProductId()).isEqualTo(expected.getProductId()),
            () -> assertThat(actual.getAmount()).isEqualTo(expected.getAmount())
        );
    }

    @DisplayName("한 회원의 위시리스트 조회")
    @Test
    void findByMemberEmail() {
        // given
        WishedProduct savedWishedProduct = wishedProductRepository.save(createWishedProduct(member, product));
        List<WishedProduct> expected = new ArrayList<>(List.of(savedWishedProduct));
        Pageable pageable = PageRequest.of(0, 5);

        // when
        List<WishedProduct> actual = wishedProductRepository.findByMember(member, pageable).toList();

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("위시리스트 상품 삭제")
    @Test
    void delete() {
        // given
        long id = wishedProductRepository.save(createWishedProduct(member, product)).getId();

        // when
        wishedProductRepository.deleteById(id);

        // then
        assertThat(wishedProductRepository.findById(id)).isEmpty();
    }

    @DisplayName("위시리스트 상품 수량 변경")
    @Test
    void update() {
        // given
        WishedProduct expected = wishedProductRepository.save(createWishedProduct(member, product));
        expected.setAmount(5);

        // when
        WishedProduct actual = wishedProductRepository.save(expected);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getMemberEmail()).isEqualTo(expected.getMemberEmail()),
            () -> assertThat(actual.getProductId()).isEqualTo(expected.getProductId()),
            () -> assertThat(actual.getAmount()).isEqualTo(expected.getAmount())
        );
    }
}
