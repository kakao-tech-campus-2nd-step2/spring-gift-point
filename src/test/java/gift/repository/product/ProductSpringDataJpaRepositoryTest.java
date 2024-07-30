package gift.repository.product;

import gift.domain.Category;
import gift.domain.Product;
import gift.repository.category.CategorySpringDataJpaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductSpringDataJpaRepositoryTest {

    @Autowired
    private ProductSpringDataJpaRepository productRepository;

    @Autowired
    private CategorySpringDataJpaRepository categoryRepository;

    @Test
    public void testFindById() {
        Category category = new Category("패션", "color", "image.url", "description");
        categoryRepository.save(category);
        Product product = new Product("상의", 1000, "상의.jpg", category);
        Product savedProduct = productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertEquals("상의", foundProduct.get().getName());
    }

    @Test
    public void testSave() {
        Category category = new Category("패션", "color", "image.url", "description");
        categoryRepository.save(category);
        Product product = new Product("상의", 20, "상의.jpg", category);

        Product savedProduct = productRepository.save(product);

        assertEquals("상의", savedProduct.getName());
    }

    @Test
    public void testDelete() {
        Category category = new Category("패션", "color", "image.url", "description");
        categoryRepository.save(category);
        Product product = new Product("상의", 50, "상의.jpg", category);
        Product savedProduct = productRepository.save(product);

        productRepository.delete(savedProduct);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertFalse(foundProduct.isPresent());
    }
}
