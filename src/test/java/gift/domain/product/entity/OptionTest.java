package gift.domain.product.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import gift.domain.product.repository.CategoryJpaRepository;
import gift.domain.product.repository.OptionJpaRepository;
import gift.domain.product.repository.ProductJpaRepository;
import gift.exception.InvalidOptionInfoException;
import gift.exception.OutOfStockException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OptionTest {

    @Autowired
    private OptionJpaRepository optionJpaRepository;

    @Autowired
    private ProductJpaRepository productJpaRepository;

    @Autowired
    private CategoryJpaRepository categoryJpaRepository;

    private Product product;

    @BeforeEach
    void setUp() {
        Category category = categoryJpaRepository.findById(1L).orElseThrow();
        product = new Product(null, category, "testProduct", 10000, "http://test.com");
        product.removeOptions();
    }

    @Test
    @DisplayName("옵션 엔티티 검증 성공")
    void prePersist_success() {
        // given
        Option option = new Option(null, product, "testOption", 100);
        product.addOption(option);

        // when
        productJpaRepository.save(product);
        Option savedOption = optionJpaRepository.save(option);

        // then
        assertThat(savedOption).isNotNull();
    }

    @Test
    @DisplayName("옵션 엔티티 검증 실패 - 상품이 null")
    void prePersist_fail_product_error() {
        // given
        Option option1 = new Option(null, product, "testOption1", 100);
        product.addOption(option1);
        productJpaRepository.save(product);
        Option option2 = new Option(null, null, "testOption2", 100);

        // when & then
        assertThatThrownBy(() -> optionJpaRepository.save(option2))
            .isInstanceOf(InvalidOptionInfoException.class);
    }

    @Test
    @DisplayName("옵션 엔티티 검증 실패 - 옵션 이름 오류")
    void prePersist_fail_name_error() {
        // given
        Option option = new Option(null, product, "#testOption", 100);
        product.addOption(option);

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidOptionInfoException.class);
    }

    @Test
    @DisplayName("옵션 엔티티 검증 실패 - 옵션 수량 오류")
    void prePersist_fail_quantity_error() {
        // given
        Option option = new Option(null, product, "testOption", -1);
        product.addOption(option);

        // when & then
        assertThatThrownBy(() -> productJpaRepository.save(product))
            .isInstanceOf(InvalidOptionInfoException.class);
    }

    @Test
    @DisplayName("옵션 수량 차감 성공")
    void subtract_success() {
        // given
        Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
        Product product = new Product(1L, category, "testProduct", 10000, "https://test.com");
        Option option = new Option(1L, product, "사과맛", 70);

        // when
        option.subtract(7);

        // then
        assertThat(option.getQuantity()).isEqualTo(63);
    }

    @Test
    @DisplayName("옵션 수량 차감 실패")
    void subtract_fail() {
        // given
        Category category = new Category(1L, "교환권", "#FFFFFF", "https://gift-s.kakaocdn.net/dn/gift/images/m640/dimm_theme.png", "test");
        Product product = new Product(1L, category, "testProduct", 10000, "https://test.com");
        Option option = new Option(1L, product, "사과맛", 70);

        // when & then
        assertThatThrownBy(() -> option.subtract(80))
            .isInstanceOf(OutOfStockException.class);
    }
}