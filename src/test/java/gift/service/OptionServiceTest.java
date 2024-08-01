package gift.service;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import gift.domain.category.Category;
import gift.domain.category.CategoryRepository;
import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.service.option.OptionService;
import gift.web.dto.OptionDto;
import gift.web.exception.ProductNotFoundException;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * - [ ] 상품이 없는 경우
 * - [ ] 이름이 50자 이상인 경우
 * - [ ] 옵션 수량이 0보다 작은 경우
 * - [ ] 옵션 수량이 1억보다 큰 경우
 * - [ ] 같은 상품의 옵션 이름이 중복되는 경우
 */
@SpringBootTest
public class OptionServiceTest {
    @Autowired
    private OptionService optionService;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private OptionRepository optionRepository;

    @Test
    void 상품이_없는_경우() {
        var optionDto = new OptionDto(null, "상품1", 999L);
        Assertions.assertThatExceptionOfType(ProductNotFoundException.class)
                .isThrownBy(() -> optionService.createOption(1L, optionDto));
    }

    @Test
    void 이름이_50자_이상인_경우() {
        // given

        var category = new Category("name", "color", "desc", "image");
        given(productRepository.findById(any())).willReturn(
            Optional.of(new Product("product", 1000L, "image.url", category)));
        var optionDto = new OptionDto(null, "상품1".repeat(50), 999L);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> optionService.createOption(1L, optionDto));

    }

    @Test
    void 같은_상품의_옵션_이름이_중복되는_경우() {
        // given

        var category = new Category("name", "color", "desc", "image");
        given(productRepository.findById(any())).willReturn(
            Optional.of(new Product("product", 1000L, "image.url", category)));
        final Option option = new Option("option", 999L, productRepository.findById(1L).get());

        given(optionRepository.findAllByProductId(any())).willReturn(
            List.of(option)
        );
        var optionDto = new OptionDto(null, option.getName(), 999L);

        // when
        // then
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> optionService.createOption(1L, optionDto));

    }
}
