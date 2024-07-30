package gift.repository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import java.util.List;
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

    private Product product1;
    private Product savedProduct1;
    private Category category;
    private Category savedCategory;
    private Option option1;
    private Option savedOption1;
    private Option option2;
    private Option savedOption2;

    @BeforeEach
    void setUp() {
        category = new Category(1L, "교환권");
        savedCategory = categoryRepository.save(category);
        product1 = new Product(null, "상품", "100", savedCategory, "https://kakao");
        savedProduct1 = productRepository.save(product1);
        option1 = new Option(null, "임시옵션1", 10L, savedProduct1);
        option2 = new Option(null, "임시옵션2", 10L, savedProduct1);
        savedOption1 = optionRepository.save(option1);
        savedOption2 = optionRepository.save(option2);
    }


    @Test
    void testSave() {
        assertAll(
            () -> assertThat(savedOption1.getId()).isNotNull(),
            () -> assertThat(savedOption1.getName()).isEqualTo(option1.getName()),
            () -> assertThat(savedOption1.getQuantity()).isEqualTo(option1.getQuantity()),
            () -> assertThat(savedOption1.getProduct()).isEqualTo(option1.getProduct())
        );
    }

    @Test
    void testFindAllByProductId() {
        List<Option> options = optionRepository.findAllByProductId(
            savedOption1.getProduct().getId());
        assertAll(
            () -> assertThat(options.size()).isEqualTo(2),
            () -> assertThat(options.get(0).getId()).isEqualTo(savedOption1.getId()),
            () -> assertThat(options.get(1).getId()).isEqualTo(savedOption2.getId())
        );
    }

    @Test
    void testFindById() {
        Option option = optionRepository.findById(savedOption1.getId()).get();
        assertAll(
            () -> assertThat(option).isNotNull(),
            () -> assertThat(option.getId()).isEqualTo(savedOption1.getId())
        );

    }

    @Test
    void testExistsByProductIdAndName() {
        boolean exists = optionRepository.existsByProductIdAndName(savedProduct1.getId(),
            option1.getName());
        assertThat(exists).isTrue();

        boolean notExists = optionRepository.existsByProductIdAndName(savedProduct1.getId(),
            "없는옵션");
        assertThat(notExists).isFalse();
    }

    @Test
    void testDeleteById() {
        optionRepository.deleteById(savedOption1.getId());
        boolean exists = optionRepository.existsById(savedOption1.getId());
        assertThat(exists).isFalse();
    }

    @Test
    void testDeleteAllByProductIdIn() {
        List<Long> productIds = List.of(savedProduct1.getId());
        optionRepository.deleteAllByProductIdIn(productIds);
        List<Option> options = optionRepository.findAllByProductId(savedProduct1.getId());
        assertThat(options.size()).isEqualTo(0);
    }
}