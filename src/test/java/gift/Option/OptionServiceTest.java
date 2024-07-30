package gift.Option;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.category.Category;
import gift.domain.category.JpaCategoryRepository;
import gift.domain.option.JpaOptionRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionService;
import gift.domain.option.dto.request.OptionRequest;
import gift.domain.option.dto.response.OptionResponse;
import gift.domain.product.JpaProductRepository;
import gift.domain.product.Product;
import gift.global.exception.BusinessException;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
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
            new Category("에티오피아산", "에티오피아 산 원두를 사용했습니다.","color code", "http://www.example.com/index.html"));

        product = productRepository.saveAndFlush(new Product("아이스 아메리카노 T", category, 4500, "description",
            "https://example.com/image.jpg"));

        option1 = optionRepository.saveAndFlush(new Option("에티오피아 커피 옵션1", 100L, product));
        option2 = optionRepository.saveAndFlush(new Option("에티오피아 커피 옵션2", 200L, product));

    }

    @Test
    @Description("상품 생성 시 옵션 입력")
    void testCreateOption() {
        // given
        Product product = productRepository.save(new Product("productName", category, 4500, "description",
            "https://example.com/image.jpg"));
        OptionRequest optionRequest1 = new OptionRequest("optionName", 100L);
        OptionRequest optionRequest2 = new OptionRequest("optionName", 100L);
        List<OptionRequest> optionRequests = new ArrayList<>();
        optionRequests.add(optionRequest1);
        optionRequests.add(optionRequest2);

        // when
        optionService.addOptionsToNewProduct(product, optionRequests);

        // then
        assertThat(product.getOptions()).hasSize(2);
        assertThat(product.getOptions().get(0).getName())
            .isEqualTo(optionRequest1.name());
        assertThat(product.getOptions().get(0).getQuantity())
            .isEqualTo(optionRequest1.quantity());
        assertThat(product.getOptions().get(1).getName())
            .isEqualTo(optionRequest2.name());
        assertThat(product.getOptions().get(1).getQuantity())
            .isEqualTo(optionRequest2.quantity());
    }

    @Test
    @Description("기존 상품에 옵션 추가")
    void testAddOption() {
        // when
        OptionRequest optionRequest = new OptionRequest("에티오피아산 커피 옵션3", 300L);

        // when
        optionService.addOptionToExistsProduct(product.getId(), optionRequest);
        flushAndClear();
        Product findProduct = productRepository.findById(product.getId()).get();

        // then
        assertThat(findProduct.getOptions()).hasSize(3);
        assertThat(findProduct.getOptions().get(2).getName())
            .isEqualTo(optionRequest.name());
        assertThat(findProduct.getOptions().get(2).getQuantity())
            .isEqualTo(optionRequest.quantity());
    }

    @Test
    @Description("옵션 수정")
    void testUpdateOption() {
        // given
        OptionRequest optionRequest = new OptionRequest("에티오피아산 커피 옵션1 - 수정", 300L);

        // when
        optionService.updateOption(product.getId(), option1.getId(), optionRequest);
        flushAndClear();
        Option findOption = optionRepository.findById(option1.getId()).get();

        // then
        assertThat(findOption.getName()).isEqualTo(optionRequest.name());
        assertThat(findOption.getQuantity()).isEqualTo(optionRequest.quantity());
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
