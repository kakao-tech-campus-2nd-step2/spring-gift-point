package gift.repository;

import gift.model.Category;
import gift.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class JpaProductTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품 저장 테스트")
    void saveProduct() {
        Category category = new Category("name", "#aaa", "https://asdfadf", "description");

        categoryRepository.save(category);

        Product product = new Product("name", 134, "https://asdf", category);
        Product real = productRepository.save(product);
        assertAll(
                () -> assertThat(real.getId()).isNotNull(),
                () -> assertThat(real.getName()).isEqualTo(product.getName()),
                () -> assertThat(real.getImageUrl()).isEqualTo(product.getImageUrl()),
                () -> assertThat(real.getPrice()).isEqualTo(product.getPrice()),
                () -> assertThat(real.getCategory().getId()).isEqualTo(product.getCategory().getId())
        );
    }
}
