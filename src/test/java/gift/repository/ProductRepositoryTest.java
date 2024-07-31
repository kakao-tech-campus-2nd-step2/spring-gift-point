package gift.repository;

import gift.model.product.Category;
import gift.model.product.Option;
import gift.model.product.Product;
import gift.model.product.ProductName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save(){
        Category category = new Category("category1");
        categoryRepository.save(category);
        Product expectedProduct = new Product(category, new ProductName("product1"),1000,"qwer.com");

        productRepository.save(expectedProduct);
        Optional<Product> actual = productRepository.findById(1L);
        assertAll(
                () -> assertThat(actual.get().getId()).isNotNull(),
                () -> assertThat(actual.get().getName()).isEqualTo(expectedProduct.getName())
        );
    }

    @Test
    void delete(){
        Category category = new Category("category1");
        categoryRepository.save(category);

        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        productRepository.save(expectedProduct);
        productRepository.delete(expectedProduct);
        Optional<Product> actual = productRepository.findById(1L);
        assertThat(actual).isEmpty();
    }

    @Test
    void update(){
        Category category = new Category("category1");
        categoryRepository.save(category);

        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");
        Product updatedProduct = new Product(category,new ProductName("product2"), 1500, "updated.com");

        Product savedProduct = productRepository.save(expectedProduct);
        savedProduct.updateProduct(updatedProduct);
        productRepository.save(savedProduct);

        Product fetchedProduct = productRepository.findById(savedProduct.getId()).orElse(null);
        assertThat(fetchedProduct.getName().getName()).isEqualTo(updatedProduct.getName().getName());
    }

    @Test
    void existsByName() {
        Category category = new Category("category1");
        categoryRepository.save(category);
        Product expectedProduct = new Product(category,new ProductName("product1"),1000,"qwer.com");

        Product savedProduct = productRepository.save(expectedProduct);
        assertThat(productRepository.existsById(savedProduct.getId())).isTrue();
    }
}