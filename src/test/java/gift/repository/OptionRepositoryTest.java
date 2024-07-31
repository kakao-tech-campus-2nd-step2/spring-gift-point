package gift.repository;

import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void testSaveAndFindById() {
        Category category = new Category("Sample Category", "Red", "sample-img-url", "Sample Description");
        categoryRepository.save(category);

        Product product = new Product("Sample Product", 100, "sample-img-url", category);
        productRepository.save(product);

        Option option = new Option("Sample Option", product, 10);
        optionRepository.save(option);

        Optional<Option> foundOption = optionRepository.findById(option.getId());

        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("Sample Option");
        assertThat(foundOption.get().getProduct().getName()).isEqualTo("Sample Product");
    }
}
