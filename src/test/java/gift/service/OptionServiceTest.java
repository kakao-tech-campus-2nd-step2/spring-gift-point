package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.category.model.Category;
import gift.exception.ResourceNotFoundException;
import gift.option.dto.OptionRequest;
import gift.option.model.Option;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import gift.product.model.Product;
import gift.product.repository.ProductRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OptionService optionService;

    private Product product;
    private Option option;
    private OptionRequest optionRequest;

    @BeforeEach
    void setUp() {
        Category category = new Category("테스트 카테고리");
        product = new Product(21L,"테스트 상품", 10000, "image.jpg", category);
        product.setId(1L);
        option = new Option("테스트 옵션", 10, product);
        option.setId(1L);
        product.getOptionList().add(option);
        optionRequest = new OptionRequest(1L, "테스트 옵션", 10, "테스트 상품");
    }

    @Test
    void createOptionTest() {
        when(productRepository.findByName(anyString())).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        Option createdOption = optionService.createOption(optionRequest);

        assertThat(createdOption).isNotNull();
        assertThat(createdOption.getName()).isEqualTo("테스트 옵션");
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    void findOptionsByProductIdTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.ofNullable(product));

        List<Option> retrievedOptions = optionService.findOptionsByProductId(1L);

        assertThat(retrievedOptions).hasSize(0);
    }

    @Test
    void deleteOptionsTest() {
        // given
        when(optionRepository.findAllByProduct(product)).thenReturn(Arrays.asList(option));
        when(productRepository.findById(product.getId())).thenReturn(Optional.ofNullable(product));

        // when
        optionService.deleteOptions(product.getId(), option.getId());

        // then
        verify(optionRepository, times(1)).delete(option);
    }



    @Test
    void retrieveOptionTest() {
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));

        Option retrievedOption = optionService.retrieveOption(1L);

        assertThat(retrievedOption).isNotNull();
        assertThat(retrievedOption.getName()).isEqualTo("테스트 옵션");
    }

    @Test
    void retrieveOptionNotFoundTest() {
        when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> optionService.retrieveOption(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("없는 옵션입니다.");
    }

    @Test
    void updateOptionTest() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(option);
        when(optionRepository.findAllByProduct(any(Product.class))).thenReturn(
            Collections.singletonList(option));

        Option updatedOption = optionService.updateOption(1L, 1L, optionRequest);

        assertThat(updatedOption).isNotNull();
        assertThat(updatedOption.getName()).isEqualTo("테스트 옵션");
    }

    @Test
    void updateOptionQuantityTest() {
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));

        boolean result = optionService.updateOptionQuantity(1L, 5);

        assertThat(result).isTrue();
        assertThat(option.getQuantity()).isEqualTo(5);
    }

    @Test
    void updateOptionQuantityNotFoundTest() {
        when(optionRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> optionService.updateOptionQuantity(1L, 5))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("존재 하지 않는 옵션입니다.");
    }
}