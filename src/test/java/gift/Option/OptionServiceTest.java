package gift.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionService;
import gift.domain.option.dto.OptionRequestDTO;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.global.exception.BusinessException;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OptionServiceTest {

    @Autowired
    private OptionService optionService;
    @Autowired
    private JpaOptionRepository optionRepository;
    @Autowired
    private JpaCategoryRepository categoryRepository;
    @Autowired
    private JpaProductRepository productRepository;
    @Autowired
    private EntityManager entityManager;

    private Category category;
    private Product product;
    private Option option1;
    private Option option2;

    @BeforeEach
    void setUp() {
        category = categoryRepository.saveAndFlush(
            new Category("에티오피아산", "에티오피아 산 원두를 사용했습니다."));

        product = productRepository.saveAndFlush(new Product("아이스 아메리카노 T", category, 4500,
            "https://example.com/image.jpg"));

        option1 = optionRepository.saveAndFlush(new Option("에티오피아 커피 옵션1", 100L, product));
        option2 = optionRepository.saveAndFlush(new Option("에티오피아 커피 옵션2", 200L, product));

    }

    @Test
    @Description("전체 옵션 목록 조회")
    void testGetOptions() {
        /// when
        List<Option> options = optionService.getOptions();

        // then
        assertThat(options).hasSize(2);
        assertThat(options.get(0)).isEqualTo(option1);
        assertThat(options.get(1)).isEqualTo(option2);
    }

    @Test
    @Description("상품 생성 시 옵션 입력")
    void testCreateOption() {
        // given
        Product product = productRepository.save(new Product("productName", category, 4500,
            "https://example.com/image.jpg"));
        OptionRequestDTO optionRequestDTO = new OptionRequestDTO("optionName", 100L);

        // when
        optionService.addOptionToNewProduct(product, optionRequestDTO);

        // then
        assertThat(product.getOptions()).hasSize(1);
        assertThat(product.getOptions().get(0).getName())
            .isEqualTo(optionRequestDTO.name());
        assertThat(product.getOptions().get(0).getQuantity())
            .isEqualTo(optionRequestDTO.quantity());
    }

    @Test
    @Description("기존 상품에 옵션 추가")
    void testAddOption() {
        // when
        OptionRequestDTO optionRequestDTO = new OptionRequestDTO("에티오피아산 커피 옵션3", 300L);

        // when
        optionService.addOptionToExistsProduct(product.getId(), optionRequestDTO);
        flushAndClear();
        Product findProduct = productRepository.findById(product.getId()).get();

        // then
        assertThat(findProduct.getOptions()).hasSize(3);
        assertThat(findProduct.getOptions().get(2).getName())
            .isEqualTo(optionRequestDTO.name());
        assertThat(findProduct.getOptions().get(2).getQuantity())
            .isEqualTo(optionRequestDTO.quantity());
    }

    @Test
    @Description("옵션 수정")
    void testUpdateOption() {
        // given
        OptionRequestDTO optionRequestDTO = new OptionRequestDTO("에티오피아산 커피 옵션1 - 수정", 300L);

        // when
        optionService.updateOption(product.getId(), option1.getId(), optionRequestDTO);
        flushAndClear();
        Option findOption = optionRepository.findById(option1.getId()).get();

        // then
        assertThat(findOption.getName()).isEqualTo(optionRequestDTO.name());
        assertThat(findOption.getQuantity()).isEqualTo(optionRequestDTO.quantity());
    }

    @Test
    @Description("옵션 수량 차감 성공")
    void testDecreaseQuantitySuccess() {
        // when
        optionService.decreaseOptionQuantity(option1.getId(), 50L);
        flushAndClear();

        Product findProduct = productRepository.findById(product.getId()).get();
        Option option = findProduct.getOptions().get(0);

        // then
        assertThat(option.getQuantity()).isEqualTo(50L);
    }

    @Test
    @Description("옵션 수량 차감 실패")
    void testDecreaseQuantityFail() {
        // when, then
        // 음수 수량 차감
        assertThrows(BusinessException.class,
            () -> optionService.decreaseOptionQuantity(option1.getId(), -50L));
        // 현재 수량보다 더 큰 수량 차감
        assertThrows(BusinessException.class,
            () -> optionService.decreaseOptionQuantity(option1.getId(), 150L));
    }

    void clear() {
        entityManager.clear();
    }

    void flush() {
        entityManager.flush();
    }

    void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }
}
