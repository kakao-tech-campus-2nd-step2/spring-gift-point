package gift;

import static org.assertj.core.api.Assertions.assertThat;

import gift.domain.Category;
import gift.domain.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("상품 추가")
    void addProduct() {
        Category category = new Category("category", "color", "image", "");
        categoryRepository.save(category);
        Product product = new Product("Test", 1000, "test.jpg", category);

        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        Product addedProduct = productRepository.findById(productId).orElseThrow();
        assertThat(addedProduct.getName()).isEqualTo("Test");
        assertThat(addedProduct.getPrice()).isEqualTo(1000);
        assertThat(addedProduct.getImageUrl()).isEqualTo("test.jpg");
    }

    @Test
    @DisplayName("상품 수정")
    void updateProduct() {
        Category category = new Category("category", "color", "image", "");
        categoryRepository.save(category);
        Product product = new Product("Test", 1000, "test.jpg", category);
        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        savedProduct.setName("update_Test");
        savedProduct.setPrice(2000);
        savedProduct.setImageUrl("update_test.jpg");

        Product updatedProduct = productRepository.findById(productId).orElseThrow();
        assertThat(updatedProduct.getName()).isEqualTo("update_Test");
        assertThat(updatedProduct.getPrice()).isEqualTo(2000);
        assertThat(updatedProduct.getImageUrl()).isEqualTo("update_test.jpg");
    }

    @Test
    @DisplayName("상품 삭제")
    void deleteProduct() {
        Category category = new Category("category", "color", "image", "");
        categoryRepository.save(category);
        Product product = new Product("Test", 1000, "test.jpg", category);        Product savedProduct = productRepository.save(product);
        Long productId = savedProduct.getId();

        productRepository.deleteById(productId);

        boolean exists = productRepository.existsById(productId);
        assertThat(exists).isFalse();
    }
}
