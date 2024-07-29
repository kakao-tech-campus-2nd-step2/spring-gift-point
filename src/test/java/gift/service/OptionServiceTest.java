package gift.service;

import gift.dto.CategoryRequest;
import gift.dto.CategoryResponse;
import gift.dto.OptionRequest;
import gift.dto.OptionResponse;
import gift.entity.Category;
import gift.entity.Option;
import gift.entity.Product;
import gift.repository.CategoryRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.validator.ProductNameValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
class OptionServiceTest {

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private OptionService optionService;
    private CategoryService categoryService;
    private ProductService productService;

    private Product product;
    private Category category;
    private Long optionId;
    private Long optionId2;

    @BeforeEach
    void setUp() {
        categoryRepository.deleteAll();
        productRepository.deleteAll();
        optionRepository.deleteAll();

        categoryService = new CategoryService(categoryRepository);
        productService = new ProductService(productRepository, new ProductNameValidator(),
            categoryService);
        optionService = new OptionService(optionRepository, productRepository);

        CategoryRequest categoryRequest = new CategoryRequest("테스트 카테고리", "#FFFFFF",
            "http://example.com/category1.jpg", "");
        CategoryResponse categoryResponse = categoryService.addCategory(categoryRequest);
        category = categoryRepository.findById(categoryResponse.getId()).orElseThrow();

        product = new Product("테스트 상품", 1000, "http://example.com/product.jpg", category);
        productRepository.save(product);

        Option option = new Option("테스트 옵션", 10, product);
        Option savedOption = optionRepository.save(option);
        Option option2 = new Option("테스트 옵션2", 10, product);
        Option savedOption2 = optionRepository.save(option2);
        optionId = savedOption.getId();
        optionId2 = savedOption2.getId();

    }

    @Test
    @DisplayName("새로운 옵션 추가")
    void testAddOption() {
        // given
        OptionRequest optionRequest = new OptionRequest("새 옵션", 5);

        // when
        OptionResponse optionResponse = optionService.addOption(product.getId(), optionRequest);

        // then
        assertThat(optionResponse).isNotNull();
        assertThat(optionResponse.getName()).isEqualTo("새 옵션");
        assertThat(optionResponse.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("옵션 목록 가져오기")
    void testGetOptions() {
        // when
        List<OptionResponse> options = optionService.getOptions(product.getId());

        // then
        assertThat(options.size()).isEqualTo(2);
        assertThat(options.get(0).getName()).isEqualTo("테스트 옵션");
    }

    @Test
    @DisplayName("옵션 삭제")
    void testDeleteOption() {
        // when
        optionService.deleteOption(optionId, product.getId());

        // then
        Optional<Option> option = optionRepository.findById(optionId);
        assertThat(option).isEmpty();
    }

    @Test
    @DisplayName("옵션 수량 감소")
    void testDecreaseOptionQuantity() {
        // when
        optionService.decreaseOptionQuantity(optionId, product.getId(), 5);

        // then
        Option updatedOption = optionRepository.findById(optionId).orElseThrow();
        assertThat(updatedOption.getQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("옵션 수량 감소 실패")
    void testDecreaseOptionQuantity_fail() {
        // when, then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.decreaseOptionQuantity(optionId, product.getId(), 15);
        });

        assertThat(exception.getMessage()).isEqualTo("재고가 부족합니다.");
    }


}