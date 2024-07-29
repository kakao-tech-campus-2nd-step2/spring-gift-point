package gift.repository;

import static org.assertj.core.api.Assertions.assertThat;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class OptionRepositoryTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private Category category;
    private Option option1;
    private Option option2;

    @BeforeEach
    void setUp() {
        optionRepository.deleteAll();
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        category = categoryRepository.save(new Category("테스트 카테고리"));
        product = productRepository.save(new Product("테스트 상품", 10000, "image.jpg", category));
        option1 = optionRepository.save(new Option("옵션1", 10, product));
        option2 = optionRepository.save(new Option("옵션2", 20, product));
    }

    @Test
    void saveOptionTest() {
        Option newOption = new Option("새 옵션", 30, product);
        Option savedOption = optionRepository.save(newOption);

        assertThat(savedOption).isNotNull();
        assertThat(savedOption.getId()).isNotNull();
        assertThat(savedOption.getName()).isEqualTo("새 옵션");
        assertThat(savedOption.getQuantity()).isEqualTo(30);
        assertThat(savedOption.getProduct()).isEqualTo(product);
    }

    @Test
    void findAllByProductTest() {
        List<Option> options = optionRepository.findAllByProduct(product);

        assertThat(options).hasSize(2);
        assertThat(options).extracting("name").containsExactlyInAnyOrder("옵션1", "옵션2");
    }

    @Test
    void findByNameTest() {
        Optional<Option> foundOption = optionRepository.findByName("옵션1");

        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("옵션1");
    }

    @Test
    void findByNameNotFoundTest() {
        Optional<Option> foundOption = optionRepository.findByName("존재하지 않는 옵션");

        assertThat(foundOption).isEmpty();
    }

    @Test
    void deleteOptionTest() {
        optionRepository.delete(option1);

        Optional<Option> deletedOption = optionRepository.findById(option1.getId());
        assertThat(deletedOption).isEmpty();
    }

    @Test
    void findAllTest() {
        List<Option> allOptions = optionRepository.findAll();

        assertThat(allOptions).hasSize(2);
        assertThat(allOptions).extracting("name").containsExactlyInAnyOrder("옵션1", "옵션2");
    }
}