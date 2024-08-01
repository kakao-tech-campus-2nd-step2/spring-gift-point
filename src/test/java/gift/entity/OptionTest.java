package gift.entity;

import gift.domain.OptionDTO;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.OptionsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Transactional
public class OptionTest {
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private OptionsRepository optionsRepository;

    private Options testOptions;
    private Product testProduct;
    private Category testCategory;

    @BeforeEach
    void setUp() {
        testCategory = new Category("test", "test", "test", "test");
        testProduct = new Product(testCategory, 1, "test", "testURL");
        categoryRepository.save(testCategory);
        testProduct = productRepository.save(testProduct);
        List<Option> optionList = new ArrayList<>();

        testOptions = new Options(testProduct, optionList);
        testOptions = optionsRepository.save(testOptions);
    }

    @Test
    void testAddOption() {
        Option newOption = new Option(new OptionDTO("new_option", 3));
        Option addedOption = testOptions.addOption(newOption);
        testOptions = optionsRepository.save(testOptions);

        assertThat(addedOption.getName()).isEqualTo("new_option");
        assertThat(addedOption.getQuantity()).isEqualTo(3);
        assertThat(testOptions.getOptions().size()).isEqualTo(1);
    }

    @Test
    void testAddDuplicatedOption() {
        Option option1 = new Option(new OptionDTO("same_name", 1));
        option1 = optionRepository.save(option1);
        testOptions.addOption(option1);
        optionsRepository.save(testOptions);

        Option duplicateOption = new Option(new OptionDTO("same_name", 4));
        duplicateOption = optionRepository.save(duplicateOption);
        Option finalDuplicateOption = duplicateOption;
        assertThatThrownBy(() -> testOptions.addOption(finalDuplicateOption))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void testUpdateOption() {
        Option option = new Option(new OptionDTO("original_name", 1));
        testOptions.addOption(option);
        testOptions = optionsRepository.save(testOptions);

        OptionDTO updatedOptionDTO = new OptionDTO("updated_name", 5);
        Option updatedOption = new Option(option.getId(), updatedOptionDTO);
        Option returnedOption = testOptions.updateOption(updatedOption);
        testOptions = optionsRepository.save(testOptions);

        assertThat(returnedOption.getName()).isEqualTo("updated_name");
        assertThat(returnedOption.getQuantity()).isEqualTo(5);
    }

    @Test
    void testDeleteOption() {
        Option option = new Option(new OptionDTO("test_name", 1));
        testOptions.addOption(option);
        testOptions = optionsRepository.save(testOptions);

        int optionId = option.getId();
        testOptions.deleteOption(optionId);
        testOptions = optionsRepository.save(testOptions);

        assertThat(testOptions.getOptions()).isEmpty();
        assertThat(optionRepository.findById(optionId)).isEmpty();
    }
}