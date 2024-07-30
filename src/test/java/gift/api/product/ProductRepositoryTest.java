package gift.api.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.api.category.domain.Category;
import gift.api.category.repository.CategoryRepository;
import gift.api.product.domain.Product;
import gift.api.product.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save() {
        Category category = categoryRepository.findById(1L).get();
        Product expected = new Product(category, "americano", 4500, "/image/americano");

        Product actual = productRepository.save(expected);

        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual).isEqualTo(expected)
        );
    }

    @Test
    void deleteById() {
        Category category = categoryRepository.findById(1L).get();
        Product product = productRepository.save(new Product(category, "name", 1000, "url"));

        Long id = productRepository.save(product).getId();
        productRepository.deleteById(id);

        assertThat(productRepository.findById(id)).isEmpty();
    }
}