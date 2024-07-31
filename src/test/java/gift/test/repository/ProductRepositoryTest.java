package gift.test.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import gift.entity.Category;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;

@DataJpaTest
public class ProductRepositoryTest {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    @Autowired
    public ProductRepositoryTest(ProductRepository productRepository, CategoryRepository categoryRepository) {
    	this.productRepository = productRepository;
    	this.categoryRepository = categoryRepository;
    }
    
    private Category category;
    private Product product;
    
    @BeforeEach
    void setUp() {
    	category = new Category("교환권", "#6c95d1", "https://example.com/image.jpg", "");
    	categoryRepository.save(category);
    	product = new Product("아이스 아메리카노 T", 4500, "https://example.com/image.jpg", category);
    }
    
    @Test
    void save() {
        Product actual = productRepository.save(product);
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(product.getName());
    }

    @Test
    void findById() {
    	productRepository.save(product);
        Product actual = productRepository.findById(product.getId()).orElse(null);
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(product.getName());
    }
}