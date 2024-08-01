package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.category.CategoryRepository;
import gift.category.model.Category;
import gift.option.OptionRepository;
import gift.option.model.Option;
import gift.product.ProductRepository;
import gift.product.model.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
@ActiveProfiles("test")
class OptionRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    private Category category;
    private Product product;
    private Option option1;
    private Option option2;

    @BeforeEach
    void setUp() {
        if (categoryRepository.findAll().isEmpty()) {
            category = new Category("category", "##test", "category.jpg", "category");
            categoryRepository.save(category);
        }
        if (productRepository.findAll().isEmpty()) {
            product = new Product("product", 1000, "product.jpg", category);
            productRepository.save(product);
        }
        if (optionRepository.findAll().isEmpty()) {
            option1 = new Option("option1", 1, product);
            option2 = new Option("option2", 2, product);
            optionRepository.save(option1);
            optionRepository.save(option2);
        }
    }

    @Test
    void findAllByProductId() {
        List<Option> options = optionRepository.findAllByProductId(product.getId());
        assertThat(options).containsExactly(option1, option2);
    }

    @Test
    void deleteById() {
        optionRepository.deleteById(2L);

        Optional<Option> option = optionRepository.findById(2L);

        assertThat(option).isEmpty();
    }
}
