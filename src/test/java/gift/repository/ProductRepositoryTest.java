package gift.repository;

import gift.entity.Category;
import gift.entity.Product;
import gift.entity.ProductName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void 상품_저장_후_조회_성공() {
        Category category = new Category("테스트", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        categoryRepository.save(category);

        Product product = new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product1.jpg", category);
        productRepository.save(product);

        Optional<Product> foundProduct = productRepository.findById(product.getId());
        assertTrue(foundProduct.isPresent());
        assertEquals("오둥이 입니다만", foundProduct.get().getName().getValue());
        assertEquals(category.getId(), foundProduct.get().getCategory().getId());
    }

    @Test
    public void 상품_삭제_성공() {
        Category category = new Category("테스트", "#FF0000", "https://example.com/test.png", "테스트 카테고리");
        categoryRepository.save(category);

        Product product = new Product(new ProductName("오둥이 입니다만"), 29800, "https://example.com/product1.jpg", category);
        productRepository.save(product);

        productRepository.delete(product);

        Optional<Product> foundProduct = productRepository.findById(product.getId());
        assertFalse(foundProduct.isPresent());
    }
}
