package gift;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.dao.DataIntegrityViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class OptionTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;

    @BeforeEach
    void setUp() {
        category = new Category("category", "color", "www.image.com", "");
        categoryRepository.save(category); // Category를 먼저 저장

        product = new Product("Test Product", 100, "http://example.com/image.jpg", category);

        Option option = new Option("Initial Option", 10, product);
        product.addOption(option); // Product에 옵션을 추가
        productRepository.save(product); // Product를 저장

        optionRepository.save(option); // 옵션 저장
    }

    // 옵션 이름이 50자 이상인 경우
    @Test
    void test1() {
        assertThatThrownBy(() -> new Option("option".repeat(50), 1, product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Option name must be less than 50 characters");
    }

    // 옵션 수량이 0인 경우
    @Test
    void test2() {
        assertThatThrownBy(() -> new Option("option", 0, product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be greater than 0");
    }

    // 옵션 수량이 1억보다 큰 경우
    @Test
    void test3() {
        assertThatThrownBy(() -> new Option("option", 100_000_001, product))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Quantity must be less than or equal to 100,000,000");
    }

    // 같은 상품의 옵션 이름이 중복될 경우
    @Test
    @Transactional
    void test4() {
        Option option1 = new Option("Option1", 10, product);
        Option option2 = new Option("Option1", 20, product);

        optionRepository.save(option1);

        assertThatThrownBy(() -> {
            optionRepository.save(option2);
        }).isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("Unique index or primary key violation");
    }


    // 상품이 존재하지 않는 경우
    @Test
    void test5() {
        assertThatThrownBy(() -> new Option("option", 10, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Product cannot be null");
    }

    // 옵션 이름에 허용되지 않은 특수 문자가 포함된 경우
    @Test
    void test6() {
        var invalidCharacters = new String[]{"!", "@", "#", "$", "%", "^", "*", "<", ">", "?"};

        for (String invalidChar : invalidCharacters) {
            assertThatThrownBy(() -> new Option("option" + invalidChar, 10, product))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Invalid characters in option name");
        }
    }
}
