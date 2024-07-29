package gift.repository;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class JpaOptionTest {
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private Product product;

    @BeforeEach
    void setUp() {
        Category category = new Category("name", "#ac5d6f", "https://asdfasdfasdf", "description");
        categoryRepository.save(category);
        product = new Product("name", 134, "https://asdfadf.com", category);
        productRepository.save(product);
    }

    @Test
    @DisplayName("옵션 저장 테스트")
    void saveOption() {
        Option option = new Option("옵션 이름", 10L, product);
        Option savedOption = optionRepository.save(option);

        assertAll(
                () -> assertThat(savedOption.getId()).isNotNull(),
                () -> assertThat(savedOption.getProduct().getId()).isEqualTo(product.getId()),
                () -> assertThat(savedOption.getName()).isEqualTo(option.getName()),
                () -> assertThat(savedOption.getQuantity()).isEqualTo(option.getQuantity())
        );
    }

    @Test
    @DisplayName("옵션 이름 중복 확인 테스트")
    void checkDuplicateOptionName() {
        Option option1 = new Option("옵션1", 10L, product);
        Option option2 = new Option("옵션2", 20L, product);

        assertThat(product.checkDuplicateOptionName("옵션1")).isTrue();
        assertThat(product.checkDuplicateOptionName("옵션2")).isTrue();
        assertThat(product.checkDuplicateOptionName("옵션3")).isFalse();
    }
}
