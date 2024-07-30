package gift.repository.option;

import gift.domain.Category;
import gift.domain.Option;
import gift.domain.Product;
import gift.repository.category.CategorySpringDataJpaRepository;
import gift.repository.product.ProductSpringDataJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Transactional
public class OptionSpringDataJpaRepositoryTest {

    @Autowired
    private OptionSpringDataJpaRepository optionRepository;

    @Autowired
    private ProductSpringDataJpaRepository productRepository;

    @Autowired
    private CategorySpringDataJpaRepository categoryRepository;

    private Product testProduct;


    @BeforeEach
    public void setUp() {
        Category testCategory = new Category("테스트 카테고리", "컬러", "image.url", "description");
        categoryRepository.save(testCategory);

        testProduct = new Product("테스트 상품", 1000, "test.jpg", testCategory);
        productRepository.save(testProduct);
    }


    @Test
    public void testFindByProductId() {
        Option option1 = new Option("옵션1", 10);
        option1.setProduct(testProduct);
        Option option2 = new Option("옵션2", 20);
        option2.setProduct(testProduct);
        optionRepository.save(option1);
        optionRepository.save(option2);

        List<Option> options = optionRepository.findByProductId(testProduct.getId());

        assertThat(options).hasSize(2);
        assertThat(options).extracting(Option::getName).containsExactly("옵션1", "옵션2");
    }

    @Test
    public void testFindByNameAndProductId() {
        Option option = new Option("옵션1", 10);
        option.setProduct(testProduct);
        optionRepository.save(option);

        Optional<Option> foundOption = optionRepository.findByNameAndProductId("옵션1", testProduct.getId());

        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("옵션1");
    }

    @Test
    public void testFindByIdAndProductId() {
        Option option = new Option("옵션1", 10);
        option.setProduct(testProduct);
        Option savedOption = optionRepository.save(option);

        Optional<Option> foundOption = optionRepository.findByIdAndProductId(savedOption.getId(), testProduct.getId());

        assertThat(foundOption).isPresent();
        assertThat(foundOption.get().getName()).isEqualTo("옵션1");
    }

    @Test
    public void testCountOptionByProductId() {
        Option option1 = new Option("옵션1", 10);
        option1.setProduct(testProduct);
        Option option2 = new Option("옵션2", 20);
        option2.setProduct(testProduct);
        optionRepository.save(option1);
        optionRepository.save(option2);

        Long count = optionRepository.countOptionByProductId(testProduct.getId());

        assertThat(count).isEqualTo(2);
    }
}
