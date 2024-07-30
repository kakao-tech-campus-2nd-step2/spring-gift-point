package gift.product;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import gift.option.Option;
import gift.option.OptionResponse;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    /*
    - [] 없는 상품의 옵션을 만들려는 경우
    - [] 이미 지워진 상품의 옵션을 삭제 하려는 경우
    - [] 등록되지 않은 옵션을 삭제 하려는 경우
     */

    @Test
    void addOption() {
        //given
        Product product = product();
        Option option = option(null, product);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        //when
        List<OptionResponse> options = productService.addOption(product.getId(), option);
        // then
        assertAll(
            () -> assertThat(options.size()).isEqualTo(product.getOptions().size()),
            () -> assertThat(options).isEqualTo(product.getOptionResponses()),
            () -> assertThat(options.contains(new OptionResponse(option))).isEqualTo(true)
        );
    }

    @Test
    void deleteOption() {
        //given
        Product product = product();
        Option option = option(1L, product);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        product.getOptions().add(option);
        //when
        List<Option> options = productService.deleteOption(product.getId(), option);
        // then
        assertAll(
            () -> assertThat(options.size()).isEqualTo(product.getOptions().size()),
            () -> assertThat(options).isEqualTo(product.getOptions()),
            () -> assertThat(options.contains(option)).isEqualTo(false)
        );
    }

    @DisplayName("등록되지 않은 옵션을 삭제 하려는 경우")
    @Test
    void deleteOptionFail2() {
        //given
        Product product = product();
        Option option = option(null, product);
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        //when// then
        assertThrows(NoSuchElementException.class,
            () -> productService.deleteOption(product.getId(), option));
    }

    private Product product() {
        return new Product(1L, "product", 1, "image", 1L);
    }

    private Option option(Long optionId, Product product) {
        return new Option(optionId, "option", 1, product);
    }

}