package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Transactional
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Test
    @DisplayName("상품 저장 및 ID로 조회")
    public void testSaveAndFindById() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "test.jpg", category);
        Product savedProduct = productRepository.save(product);

        Option option1 = new Option("Option1", 100, savedProduct);
        Option option2 = new Option("Option2", 200, savedProduct);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct).isPresent();
        assertThat(foundProduct.get().getName()).isEqualTo("Test Product");
        assertThat(foundProduct.get().getCategoryName()).isEqualTo("Test Category");
    }

    @Test
    @DisplayName("모든 상품 조회")
    public void testFindAll() {
        long initialCount = productRepository.count();

        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product1 = new Product("Product 1", 100, "prod1.jpg", category);
        Product product2 = new Product("Product 2", 200, "prod2.jpg", category);
        productRepository.save(product1);
        productRepository.save(product2);

        Option option1 = new Option("Option1", 100, product1);
        Option option2 = new Option("Option2", 200, product1);
        Option option3 = new Option("Option3", 300, product2);
        Option option4 = new Option("Option4", 400, product2);
        optionRepository.save(option1);
        optionRepository.save(option2);
        optionRepository.save(option3);
        optionRepository.save(option4);

        Iterable<Product> products = productRepository.findAll();
        assertThat(products).hasSize((int) initialCount + 2);
    }

    @Test
    @DisplayName("상품 삭제")
    public void testDelete() {
        Category category = new Category("Test Category", "#000000", "imageUrl", "description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "test.jpg", category);
        Product savedProduct = productRepository.save(product);

        Option option1 = new Option("Option1", 100, savedProduct);
        Option option2 = new Option("Option2", 200, savedProduct);
        optionRepository.save(option1);
        optionRepository.save(option2);

        productRepository.deleteById(savedProduct.getId());
        Optional<Product> foundProduct = productRepository.findById(savedProduct.getId());

        assertThat(foundProduct).isNotPresent();
    }
}
