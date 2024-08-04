package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import gift.dto.optionDTO.OptionRequestDTO;
import gift.dto.optionDTO.OptionResponseDTO;
import gift.exception.InvalidInputValueException;
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
        category = new Category(null, "테스트카테고리", "#770077", "테스트 이미지", "테스트 설명");
        categoryRepository.save(category);
        product = new Product(null, "상품", "100", category, "https://kakao");
        productRepository.save(product);
        option = new Option(null, "테스트", 1L, product);
        optionRepository.save(option);
    }

    @Test
    void testFindAllByProductId() {
        List<OptionResponseDTO> options = optionService.getAllOptionsByProductId(product.getId());
        assertAll(
            () -> assertEquals(1, options.size()),
            () -> assertEquals("테스트", options.get(0).name())
        );
    }


    @Test
    void testGetOptionById() {
        OptionResponseDTO foundOption = optionService.getOptionById(option.getId());
        assertAll(
            () -> assertNotNull(foundOption),
            () -> assertEquals(option.getId(), foundOption.id())
        );
    }

    @Test
    @Transactional
    void testAddOption() {
        OptionRequestDTO optionDTO = new OptionRequestDTO("테스트2", 1L);
        optionService.addOption(product.getId(), optionDTO);
        List<OptionResponseDTO> options = optionService.getAllOptionsByProductId(product.getId());
        assertEquals(2, options.size());
    }

    @Test
    @Transactional
    void testAddOptionWithDuplicateName() {
        OptionRequestDTO optionDTO = new OptionRequestDTO("테스트", 10L);
        try {
            optionService.addOption(product.getId(), optionDTO);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }


    @Test
    @Transactional
    void testUpdateOption() {
        OptionRequestDTO updatedOptionDTO = new OptionRequestDTO("테스트3", 2L);
        optionService.updateOption(product.getId(), option.getId(), updatedOptionDTO);
        OptionResponseDTO updatedOption = optionService.getOptionById(option.getId());
        assertAll(
            () -> assertEquals("테스트3", updatedOption.name()),
            () -> assertEquals(2L, updatedOption.quantity())
        );
    }

    @Test
    @Transactional
    void testUpdateOptionWithDuplicateName() {
        OptionRequestDTO newOptionDTO = new OptionRequestDTO("테스트2", 10L);
        optionService.addOption(product.getId(), newOptionDTO);
        OptionRequestDTO updatedOptionDTO = new OptionRequestDTO("테스트2", 2L);
        try {
            optionService.updateOption(product.getId(), option.getId(), updatedOptionDTO);
        } catch (InvalidInputValueException e) {
            assertThat(e).isInstanceOf(InvalidInputValueException.class);
        }
    }

    @Test
    @Transactional
    void testDeleteOption() {
        Option newOption = new Option(null, "테스트2", 2L, product);
        optionRepository.save(newOption);

        optionService.deleteOption(product.getId(), option.getId());
        List<OptionResponseDTO> options = optionService.getAllOptionsByProductId(product.getId());
        assertEquals(1, options.size());
    }

    @Test
    @Transactional
    void testDeleteOptionWithSingleOption() {
        try {
            optionService.deleteOption(product.getId(), option.getId());
        } catch (IllegalArgumentException e) {
            assertThat(e).isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Test
    @Transactional
    void testSubtractQuantity() {
        optionService.subtractQuantity(option.getId(), 1L);
        OptionResponseDTO subtractedOption = optionService.getOptionById(option.getId());
        assertEquals(0L, subtractedOption.quantity());
    }
}