package gift.repository;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class OptionRepositoryTest {
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    private Product product;

    @BeforeEach
    void init() {
        Category category = new Category("신규");
        categoryRepository.save(category);

        product = new Product("물품 1", 4500, "none", category);
        productRepository.save(product);
    }

    @Test
    @DisplayName("saveTest")
    void test1() {
        // when
        Option option = new Option("name", 1000L);
        product.addOption(option);
        Option actual = optionRepository.save(option);
        // then
        Assertions.assertThat(option).isNotNull();
        Assertions.assertThat(actual).isEqualTo(option);
        Assertions.assertThat(actual.getQuantity()).isEqualTo(option.getQuantity());
        Assertions.assertThat(actual.getName()).isEqualTo(option.getName());
    }

    @Test
    @DisplayName("findByIdTest")
    void test2(){
        // given
        Option option = new Option("name", 1000L);
        product.addOption(option);
        Option actual = optionRepository.save(option);
        // when
        Option savedOption = optionRepository.findById(1L).orElseThrow(NoSuchFieldError::new);
        // then
        Assertions.assertThat(savedOption).isNotNull();
        Assertions.assertThat(savedOption).isEqualTo(actual);
    }
    @Test
    @DisplayName("deleteTest")
    void test3(){
        // given
        Option option = new Option("name", 1000L);
        product.addOption(option);
        Option actual = optionRepository.save(option);
        // when
        optionRepository.deleteById(1L);
        // then
        boolean tf = optionRepository.existsById(1L);
        Assertions.assertThat(tf).isEqualTo(false);
    }
}
