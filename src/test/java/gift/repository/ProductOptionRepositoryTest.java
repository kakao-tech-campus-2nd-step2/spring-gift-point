package gift.repository;

import gift.entity.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ProductOptionRepositoryTest {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @AfterEach
    public void tearDown() {
        productOptionRepository.deleteAll();
        productRepository.deleteAll();
        optionRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    public void 상품_옵션_저장_및_조회_성공() {
        Category category = categoryRepository.save(new Category("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Product product = productRepository.save(new Product(new ProductName("상품1"), 1000, "https://example.com/product1.jpg", category));
        Option option = optionRepository.save(new Option(new OptionName("옵션1")));

        ProductOption productOption = new ProductOption(product, option, 10);
        productOptionRepository.save(productOption);

        boolean exists = productOptionRepository.existsByProductAndOptionName(product, new OptionName("옵션1"));
        assertTrue(exists);
    }

    @Test
    public void 상품_옵션_삭제_성공() {
        Category category = categoryRepository.save(new Category("테스트카테고리", "#FF0000", "https://example.com/test.png", "테스트 카테고리"));
        Product product = productRepository.save(new Product(new ProductName("상품1"), 1000, "https://example.com/product1.jpg", category));
        Option option = optionRepository.save(new Option(new OptionName("옵션1")));

        ProductOption productOption = new ProductOption(product, option, 10);
        productOptionRepository.save(productOption);

        productOptionRepository.delete(productOption);
        boolean exists = productOptionRepository.existsByProductAndOptionName(product, new OptionName("옵션1"));
        assertFalse(exists);
    }
}
