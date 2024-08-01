package gift.repository;

import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        Option expected = new Option("Test Option", 100, product);
        Option actual = optionRepository.save(expected);
        assertAll(
                () -> assertThat(actual.getId()).isNotNull(),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName()),
                () -> assertThat(actual.getQuantity()).isEqualTo(expected.getQuantity()),
                () -> assertThat(actual.getProduct()).isEqualTo(expected.getProduct())
        );
    }

    @Test
    void findById() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        Option savedOption = optionRepository.save(new Option("Test Option", 100, product));
        Optional<Option> actual = optionRepository.findById(savedOption.getId());
        assertThat(actual).isPresent();
        actual.ifPresent(option -> assertAll(
                () -> assertThat(option.getName()).isEqualTo("Test Option"),
                () -> assertThat(option.getQuantity()).isEqualTo(100),
                () -> assertThat(option.getProduct()).isEqualTo(product)
        ));
    }

    @Test
    void findByProductId() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        optionRepository.save(new Option("Option 1", 100, product));
        optionRepository.save(new Option("Option 2", 200, product));

        List<Option> options = optionRepository.findByProductId(product.getId());
        assertThat(options).hasSize(2);
    }

    @Test
    void deleteById() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        Option savedOption = optionRepository.save(new Option("Option to delete", 100, product));
        optionRepository.deleteById(savedOption.getId());
        Optional<Option> deletedOption = optionRepository.findById(savedOption.getId());
        assertThat(deletedOption).isNotPresent();
    }

    @Test
    void updateOption() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        Option savedOption = optionRepository.save(new Option("Original Option", 100, product));
        savedOption.setName("Updated Option");
        savedOption.setQuantity(200);

        Option updatedOption = optionRepository.save(savedOption);

        assertAll(
                () -> assertThat(updatedOption.getName()).isEqualTo("Updated Option"),
                () -> assertThat(updatedOption.getQuantity()).isEqualTo(200),
                () -> assertThat(updatedOption.getProduct()).isEqualTo(product)
        );
    }

    @Test
    void subtractQuantity() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        Option option = new Option("Test Option", 100, product);
        optionRepository.save(option);

        option.subtractQuantity(50);
        optionRepository.save(option);

        Option updatedOption = optionRepository.findById(option.getId()).orElseThrow();
        assertThat(updatedOption.getQuantity()).isEqualTo(50);
    }

    @Test
    void subtractQuantityExceedsAvailable() {
        Category category = new Category("Test Category", "#FFFFFF", "http://example.com/cat.jpg", "Description");
        categoryRepository.save(category);

        Product product = new Product("Test Product", 100, "http://example.com/test.jpg", category);
        productRepository.save(product);

        Option option = new Option("Test Option", 100, product);
        optionRepository.save(option);

        assertThrows(IllegalArgumentException.class, () -> {
            option.subtractQuantity(150);
        });
    }
}