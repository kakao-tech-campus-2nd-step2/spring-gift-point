package gift.Product;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest
public class OptionRepositoryTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OptionRepository optionRepository;

    private static final String CATEGORY_NAME = "교환권";
    private static final String PRODUCT_NAME = "카푸치노";
    private static final int PRODUCT_PRICE = 3000;
    private static final String PRODUCT_URL = "example.com";
    private static final String OPTION_NAME = "large";
    private static final Long OPTION_QUANTITY = 3L;
    private static final String OPTION_NAME2 = "small";
    private static final Long OPTION_QUANTITY2 = 5L;


    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        Category category = new Category(CATEGORY_NAME);
        category.setId(1L);

        when(categoryRepository.findByCategoryName(CATEGORY_NAME)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
    }

    @Test
    void optionAddTest(){

        Category category = categoryRepository.findByCategoryName(CATEGORY_NAME).orElseThrow();
        Product expected = new Product(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_URL, category);
        expected.setId(2L);
        Option option = new Option(OPTION_NAME, OPTION_QUANTITY, expected);
        option.setId(3L);
        List<Option> options = new ArrayList<>();
        expected.setOptions(options);
        options.add(option);
        optionRepository.saveAll(options);

        assertThat(options.contains(option)).isTrue();
    }

    @Test
    void deleteTest(){
        Category category = categoryRepository.findByCategoryName(CATEGORY_NAME).orElseThrow();
        Product expected = new Product(PRODUCT_NAME, PRODUCT_PRICE, PRODUCT_URL, category);
        expected.setId(2L);
        Option option = new Option(OPTION_NAME, OPTION_QUANTITY, expected);
        option.setId(3L);
        optionRepository.save(option);
        optionRepository.deleteById(option.getId());
        when(optionRepository.findById(option.getId())).thenReturn(Optional.empty());

        Optional<Option> actual = optionRepository.findById(option.getId());

        assertThat(actual).isNotPresent();
    }

}
