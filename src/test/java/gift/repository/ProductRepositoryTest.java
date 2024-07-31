package gift.repository;

import gift.entity.Category;
import gift.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product expected = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        Product actual = productRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getPrice()).isEqualTo(expected.getPrice()),
                () -> assertThat(actual.getImageUrl()).isEqualTo(expected.getImageUrl()),
                () -> assertThat(actual.getCategory()).isEqualTo(expected.getCategory())
        );
    }

    @Test
    void findById() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product savedProduct = productRepository.save(new Product("Test Product", 100, "http://example.com/test.jpg", category));
        Optional<Product> actual = productRepository.findById(savedProduct.getId());
        assertThat(actual).isPresent();
        actual.ifPresent(product -> assertAll(
                () -> assertThat(product.getName()).isEqualTo("Test Product"),
                () -> assertThat(product.getCategory()).isEqualTo(category)
        ));
    }

    @Test
    void findAll() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        productRepository.save(new Product("Product 1", 100, "http://example.com/1.jpg", category));
        productRepository.save(new Product("Product 2", 200, "http://example.com/2.jpg", category));
        List<Product> products = productRepository.findAll();
        assertThat(products).hasSize(2);
    }

    @Test
    void deleteById() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product savedProduct = productRepository.save(new Product("Product", 100, "http://example.com/delete.jpg", category));
        productRepository.deleteById(savedProduct.getId());
        Optional<Product> deletedProduct = productRepository.findById(savedProduct.getId());
        assertThat(deletedProduct).isNotPresent();
    }

    @Test
    void updateProduct() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product savedProduct = productRepository.save(new Product("Original Name", 100, "http://example.com/original.jpg", category));
        savedProduct.setName("Updated Name");
        savedProduct.setPrice(200);
        savedProduct.setImageUrl("http://example.com/updated.jpg");

        Product updatedProduct = productRepository.save(savedProduct);

        assertAll(
                () -> assertThat(updatedProduct.getName()).isEqualTo("Updated Name"),
                () -> assertThat(updatedProduct.getPrice()).isEqualTo(200),
                () -> assertThat(updatedProduct.getImageUrl()).isEqualTo("http://example.com/updated.jpg"),
                () -> assertThat(updatedProduct.getCategory()).isEqualTo(category)
        );
    }
}
