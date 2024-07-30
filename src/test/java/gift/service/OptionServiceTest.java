package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.category.model.Category;
import gift.common.exception.OptionException;
import gift.option.OptionRepository;
import gift.option.OptionService;
import gift.option.model.Option;
import gift.option.model.OptionRequest;
import gift.product.ProductRepository;
import gift.product.model.Product;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@DataJpaTest
@DirtiesContext(classMode = ClassMode.BEFORE_CLASS)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    private OptionService optionService;

    @BeforeEach
    void setUp() {
        optionService = new OptionService(optionRepository, productRepository);
    }

    @Test
    void getOptions() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "product.jpg", category);
        given(optionRepository.findAllByProductId(any())).willReturn(
            List.of(new Option("option", 100, product)));

        optionService.getOptions(1L);

        then(optionRepository).should().findAllByProductId(any());
    }

    @Test
    void addOption() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "product.jpg", category);
        given(productRepository.findById(any())).willReturn(Optional.of(product));

        optionService.addOption(1L, new OptionRequest.Create("option", 1));

        then(optionRepository).should().save(any());
    }

    @Test
    void updateOption() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "product.jpg", category);
        given(optionRepository.findById(any())).willReturn(
            Optional.of(new Option("option", 100, product)));

        optionService.updateOption(1L, new OptionRequest.Update("change", 500));

        then(optionRepository).should().findById(any());
    }

    @Test
    void deleteOption() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "product.jpg", category);
        given(optionRepository.findById(any())).willReturn(
            Optional.of(new Option("option", 100, product)));

        assertThatExceptionOfType(OptionException.class).isThrownBy(
            () -> optionService.deleteOption(1L)
        );
    }

    @Test
    void subtractOption_옵션_수량보다_더_많이_빼는_경우() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "product.jpg", category);
        given(optionRepository.findById(any())).willReturn(
            Optional.of(new Option("option", 2, product)));

        assertThatExceptionOfType(OptionException.class).isThrownBy(
            () -> optionService.subtractOption(1L, new OptionRequest.Subtract(3))
        );
    }

    @Test
    void subtractOption() {
        Category category = new Category("category", "##cate", "category.jpg", "category");
        Product product = new Product("product", 1000, "product.jpg", category);
        Option option = new Option("option", 2, product);
        given(optionRepository.findById(any())).willReturn(
            Optional.of(option));

        optionService.subtractOption(1L, new OptionRequest.Subtract(1));

        assertThat(option.getQuantity()).isEqualTo(1);
    }
}
