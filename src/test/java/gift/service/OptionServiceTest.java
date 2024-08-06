package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import gift.domain.category.Category;
import gift.domain.category.CategoryRequest;
import gift.domain.option.OptionRequest;
import gift.domain.option.OptionResponse;
import gift.domain.product.ProductRequest;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@Transactional
public class OptionServiceTest {

    @Autowired
    OptionService optionService;
    @Autowired
    ProductService productService;
    @Autowired
    CategoryService categoryService;

    private Long productId1, productId2;

    @BeforeEach
    public void setUp() {
        Category category = categoryService.save(
            new CategoryRequest("카테고리 1", "#123456", "ImageUrl", "description"));
        productId1 = productService.save(
            new ProductRequest("상품 1", 10000L, "imageUrl", category.getId(), null)).getId();
        productId2 = productService.save(
            new ProductRequest("상품 1", 10000L, "imageUrl", category.getId(), null)).getId();
    }

    @Test
    @DisplayName("정상: 저장")
    void 정상_저장() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 10L);

        OptionResponse optionResponse = optionService.save(productId1, optionRequest);

        assertThat(optionResponse.id()).isNotNull();
    }

    @Test
    @DisplayName("예외: 상품 내 옵션 이름 중복 저장")
    void 예외_상품_내_옵션_이름_중복_저장() {
        OptionRequest optionRequest1 = new OptionRequest("옵션 1", 10L);
        OptionRequest optionRequest2 = new OptionRequest("옵션 1", 10L);
        optionService.save(productId1, optionRequest1);

        assertThrows(ResponseStatusException.class, () ->
            optionService.save(productId1, optionRequest2)
        );
    }

    @Test
    @DisplayName("정상: 다른 상품 옵션 이름 중복 저장")
    void 정상_다른_상품_옵션_이름_중복_저장() {
        OptionRequest optionRequest1 = new OptionRequest("옵션 1", 10L);
        OptionRequest optionRequest2 = new OptionRequest("옵션 1", 10L);

        OptionResponse optionResponse1 = optionService.save(productId1, optionRequest1);
        OptionResponse optionResponse2 = optionService.save(productId2, optionRequest1);

        assertThat(optionResponse1.id()).isNotNull();
        assertThat(optionResponse2.id()).isNotNull();
    }

    @Test
    @DisplayName("정상: 수정")
    void 정상_수정() {
        OptionRequest optionRequest1 = new OptionRequest("옵션 1", 10L);
        OptionResponse optionResponse = optionService.save(productId1, optionRequest1);
        OptionRequest optionRequest2 = new OptionRequest("옵션 2", 10L);

        optionResponse = optionService.update(productId1, optionResponse.id(), optionRequest2);

        assertThat(optionResponse.name()).isEqualTo("옵션 2");
    }

    @Test
    @DisplayName("예외: 상품 내 옵션 이름 중복 수정")
    void 예외_상품_내_욥션_이름_중복_수정() {
        OptionRequest optionRequest1 = new OptionRequest("옵션 1", 10L);
        OptionResponse optionResponse = optionService.save(productId1, optionRequest1);
        OptionRequest optionRequest2 = new OptionRequest("옵션 1", 10L);

        assertThrows(ResponseStatusException.class, () ->
            optionService.update(productId1, optionResponse.id(), optionRequest2)
        );
    }

    @Test
    @DisplayName("정상: 상품 옵션 읽기")
    void 정상_상품_옵션_읽기() {
        OptionRequest optionRequest1 = new OptionRequest("옵션 1", 10L);
        OptionRequest optionRequest2 = new OptionRequest("옵션 2", 10L);
        optionService.save(productId1, optionRequest1);
        optionService.save(productId1, optionRequest2);

        List<OptionResponse> optionResponseList = optionService.findAllByProductId(productId1);

        assertThat(
            optionResponseList
                .stream()
                .map(OptionResponse::name)
                .toList())
            .contains("옵션 1", "옵션 2");
    }

    @Test
    @DisplayName("정상: 옵션 수량 차감")
    void 정상_옵션_수량_차감() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 10L);
        Long optionId = optionService.save(productId1, optionRequest).id();

        OptionResponse optionResponse = optionService.subtractQuantity(optionId, 1L);

        assertThat(optionResponse.quantity()).isEqualTo(9L);
    }

    @Test
    @DisplayName("정상: 옵션 수량 반복 차감")
    void 정상_옵션_수량_반복_차감() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 1000L);
        Long optionId = optionService.save(productId1, optionRequest).id();
        OptionResponse optionResponse = null;

        for (int i = 0; i < 500; i++) {
            optionResponse = optionService.subtractQuantity(optionId, 1L);
        }

        assertThat(optionResponse.quantity()).isEqualTo(500L);
    }

    @Test
    @DisplayName("예외: 옵션 수량 음수 차감")
    void 예외_옵션_수량_음수_차감() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 10L);
        Long optionId = optionService.save(productId1, optionRequest).id();

        assertThrows(ResponseStatusException.class, () ->
            optionService.subtractQuantity(optionId, -1L)
        );
    }

    @Test
    @DisplayName("예외: 옵션 수량 초과 차감")
    void 예외_옵션_수량_초과_차감() {
        OptionRequest optionRequest = new OptionRequest("옵션 1", 10L);
        Long optionId = optionService.save(productId1, optionRequest).id();

        assertThrows(ResponseStatusException.class, () ->
            optionService.subtractQuantity(optionId, 11L)
        );
    }

    @Test
    @DisplayName("정상: 삭제")
    void 정상_삭제() {
        OptionRequest optionRequest1 = new OptionRequest("옵션 1", 10L);
        OptionResponse optionResponse = optionService.save(productId1, optionRequest1);

        optionService.delete(productId1, optionResponse.id());

        assertThat(optionService.findAllByProductId(productId1)
            .stream()
            .map(OptionResponse::id))
            .doesNotContain(optionResponse.id());
    }
}
