package gift.repositoryTest;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindProduct() {
        Category category = new Category("TestCategory");
        categoryRepository.save(category);

        Option option1 = new Option("Option1", 10);
        Option option2 = new Option("Option2", 20);

        Product product = new Product("상품1", 1000, "http://example.com/image.jpg", category, Arrays.asList(option1, option2));
        productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(product.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("상품1");
    }

    @Test
    void testSaveValidNameProduct() {
        Category category = new Category("TestCategory");
        categoryRepository.save(category);
        Option option1 = new Option("Option1", 10);
        Option option2 = new Option("Option2", 20);

        assertThrows(IllegalArgumentException.class, () -> {
            Product product = new Product("카카오", 1000, "http://example.com/image.jpg", category, Arrays.asList(option1, option2));
            productRepository.save(product);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Product product = new Product("kakao", 1000, "http://example.com/image.jpg", category, Arrays.asList(option1, option2));
            productRepository.save(product);
        });

        Product validProduct = new Product("상품2", 2000, "http://example.com/image2.jpg", category, Arrays.asList(option1, option2));
        productRepository.save(validProduct);

        Optional<Product> foundProduct = productRepository.findById(validProduct.getId());
        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("상품2");
    }
}
