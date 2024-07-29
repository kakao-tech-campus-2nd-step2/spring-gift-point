package gift.RepositoryTest;

import gift.Model.Entity.Category;
import gift.Model.Entity.Option;
import gift.Model.Entity.Product;
import gift.Repository.CategoryRepository;
import gift.Repository.OptionRepository;
import gift.Repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
public class OptionRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;

    @BeforeEach
    void beforeEach(){
        Category category = categoryRepository.save(new Category("카테고리", "#812f3D", "카테고리 url", "카테고리 description"));
        product = productRepository.save(new Product("상품", 4000, "상품url", category));
    }

    @Test
    void saveTest(){
        Option option = new Option("옵션1", 1, product);
        assertThat(option.getId()).isNull();
        Option actual = optionRepository.save(option);
        assertThat(option.getId()).isNotNull();
    }

    @Test
    void findByIdTest(){
        Option option = optionRepository.save(new Option("옵션1", 1, product));
        Optional<Option> actual = optionRepository.findById(option.getId());

        assertThat(actual.get().getName().getValue()).isEqualTo("옵션1");
    }

    @Test
    void findByProduct(){
        Option option1 = optionRepository.save(new Option("옵션1", 1, product));
        Option option2 = optionRepository.save(new Option("옵션2", 2, product));
        List<Option> actual = optionRepository.findByProduct(product);

        assertAll(
                ()-> assertThat(actual.get(0).getName().getValue()).isEqualTo("옵션1"),
                () -> assertThat(actual.get(0).getQuantity().getValue()).isEqualTo(1),
                () -> assertThat(actual.get(1).getName().getValue()).isEqualTo("옵션2"),
                () -> assertThat(actual.get(1).getQuantity().getValue()).isEqualTo(2)
        );
    }

    @Test
    void updateTest(){
        Option option1 = optionRepository.save(new Option("옵션1", 1, product));
        Optional<Option> optionalOption = optionRepository.findById(option1.getId());
        Option option = optionalOption.get();
        option.update("옵션2", 2);

        Optional<Option> actualOptionalOption = optionRepository.findById(option1.getId());
        Option actual = actualOptionalOption.get();
        assertAll(
                () -> assertThat(actual.getName().getValue()).isEqualTo("옵션2"),
                () -> assertThat(actual.getQuantity().getValue()).isEqualTo(2)
        );
    }

    @Test
    void deleteByProduct(){
        Option option1 = optionRepository.save(new Option("옵션1", 1, product));
        Option option2 = optionRepository.save(new Option("옵션2", 2, product));
        optionRepository.deleteByProduct(product);

        Optional<Option> actual1 = optionRepository.findById(option1.getId());
        Optional<Option> actual2 = optionRepository.findById(option2.getId());
        assertAll(
                ()-> assertThat(actual1).isEmpty(),
                ()-> assertThat(actual2).isEmpty()
        );
    }

    @Test
    void deleteById(){
        Option option = optionRepository.save(new Option("옵션1", 1, product));
        optionRepository.deleteByProduct(product);
        Optional<Option> actual = optionRepository.findById(option.getId());
        assertThat(actual).isEmpty();
    }
}
