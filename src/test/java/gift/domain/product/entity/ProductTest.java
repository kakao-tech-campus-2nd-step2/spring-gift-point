package gift.domain.product.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.domain.product.repository.CategoryJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.exception.InvalidProductInfoException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProductTest {

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private Category category;
    private Option option;

    @BeforeEach
    void setUp() {
        category = categoryJpaRepository.findById(1L).orElseThrow();
        option = new Option(null, null, "testOption", 100);
    }

    @Test
    @DisplayName("상품 엔티티 검증 성공")
    void prePersist_success() {
        // given
        Product product = new Product(null, category, "testProduct", 10000, "http://test.com");
        product.addOption(option);

        // when
        Product savedProduct = productJpaRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @Test
    @DisplayName("상품 엔티티 검증 실패 - 카테고리가 null")
    void prePersist_fail_category_error() {
        // given
        Product product = new Product(null, null, "testProduct", 10000, "http://test.com");
        product.addOption(option);

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidProductInfoException.class);
    }

    @Test
    @DisplayName("상품 엔티티 검증 실패 - 상품 이름 오류")
    void prePersist_fail_name_error() {
        // given
        Product product = new Product(null, category, "#testProduct", 10000, "http://test.com");
        product.addOption(option);

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidProductInfoException.class);
    }

    @Test
    @DisplayName("상품 엔티티 검증 실패 - 상품 가격 오류")
    void prePersist_fail_price_error() {
        // given
        Product product = new Product(null, category, "testProduct", 0, "http://test.com");
        product.addOption(option);

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidProductInfoException.class);
    }

    @Test
    @DisplayName("상품 엔티티 검증 실패 - 상품 이미지 주소 오류")
    void prePersist_fail_imageUrl_error() {
        // given
        Product product = new Product(null, category, "testProduct", 10000, "//test.com");
        product.addOption(option);

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidProductInfoException.class);
    }

    @Test
    @DisplayName("상품 엔티티 검증 실패 - 상품 옵션 목록 오류")
    void prePersist_fail_options_error() {
        // given
        Product product = new Product(null, category, "testProduct", 10000, "http://test.com");

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidProductInfoException.class);
    }
}