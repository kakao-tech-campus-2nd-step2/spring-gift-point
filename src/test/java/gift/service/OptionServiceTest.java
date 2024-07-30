package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.dto.OptionDTO;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import org.junit.jupiter.api.Test;


@SpringBootTest
@Transactional(readOnly = true)
class OptionServiceTest {

    @Autowired
    private OptionService optionService;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Product product;
    private Option option;
    private Category category;

    @BeforeEach
    void setUp() {
        optionRepository.deleteAll();
        category = new Category(null, "테스트카테고리");
        categoryRepository.save(category);
        product = new Product(null, "상품", "100", category, "https://kakao");
        productRepository.save(product);
        option = new Option(null, "테스트", 1L, product);
        optionRepository.save(option);
    }

    @Test
    void testFindAllByProductId() {
        List<Option> options = optionService.findAllByProductId(product.getId());
        assertAll(
            () -> assertEquals(1, options.size()),
            () -> assertEquals("테스트", options.get(0).getName())
        );
    }

    @Test
    void testFindOptionById() {
        Option foundOption = optionService.findOptionById(option.getId());
        assertAll(
            () -> assertNotNull(foundOption),
            () -> assertEquals(5, foundOption.getId())
        );
    }

    @Test
    @Transactional
    void testSaveOption() {
        OptionDTO optionDTO = new OptionDTO("테스트2", 1L, product.getId());
        optionService.saveOption(optionDTO);
        List<Option> options = optionService.findAllByProductId(product.getId());
        assertEquals(2, options.size());
    }

    @Test
    @Transactional
    void testSaveOptionWithDuplicateName() {
        OptionDTO optionDTO = new OptionDTO("테스트", 10L, product.getId());
        assertThrows(IllegalArgumentException.class,
            () -> optionService.saveOption(optionDTO));
    }

    @Test
    @Transactional
    void testUpdateOption() {
        OptionDTO updatedOptionDTO = new OptionDTO("테스트3", 2L, product.getId());
        optionService.updateOption(updatedOptionDTO, option.getId());
        Option updatedOption = optionService.findOptionById(option.getId());
        assertAll(
            () -> assertEquals("테스트3", updatedOption.getName()),
            () -> assertEquals(2L, updatedOption.getQuantity())
        );
    }

    @Test
    @Transactional
    void testUpdateOptionWithDuplicateName() {
        OptionDTO newOptionDTO = new OptionDTO("테스트2", 10L, product.getId());
        optionService.saveOption(newOptionDTO);
        OptionDTO updatedOptionDTO = new OptionDTO("테스트2", 2L, product.getId());
        assertThrows(IllegalArgumentException.class,
            () -> optionService.updateOption(updatedOptionDTO, option.getId()));
    }

    @Test
    @Transactional
    void testDeleteOption() {
        Option newOption = new Option(null, "테스트2", 2L, product);
        optionRepository.save(newOption);

        optionService.deleteOption(option.getId(), product.getId());
        List<Option> options = optionService.findAllByProductId(product.getId());
        assertEquals(1, options.size());
    }

    @Test
    @Transactional
    void testDeleteOptionWithSingleOption() {
        assertThrows(IllegalArgumentException.class,
            () -> optionService.deleteOption(option.getId(), product.getId()));
    }

    @Test
    @Transactional
    void testSubtractQuantity() {
        optionService.subtractQuantity(option.getId(), 1L);
        Option subtractedOption = optionService.findOptionById(option.getId());
        assertEquals(0L, subtractedOption.getQuantity());
    }
}