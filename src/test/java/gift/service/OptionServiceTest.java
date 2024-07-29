package gift.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import gift.dto.OptionRequest;
import gift.exception.ResourceNotFoundException;
import gift.model.Category;
import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import java.util.Arrays;
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
    void retreiveOptionsTest() {
        List<Option> options = Arrays.asList(option);
        when(optionRepository.findAll()).thenReturn(options);

        List<Option> retrievedOptions = optionService.retreiveOptions();

        assertThat(retrievedOptions).hasSize(1);
        assertThat(retrievedOptions.get(0).getName()).isEqualTo("테스트 옵션");
    }

    @Test
    void deleteOptionsTest() {
        when(optionRepository.existsById(anyLong())).thenReturn(true);

        optionService.deleteOptions(1L);

        verify(optionRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteOptionsNotFoundTest() {
        when(optionRepository.existsById(anyLong())).thenReturn(false);

        assertThatThrownBy(() -> optionService.deleteOptions(1L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessage("없는 옵션입니다.");
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
        when(optionRepository.findById(anyLong())).thenReturn(Optional.of(option));
        when(productRepository.findByName(anyString())).thenReturn(Optional.of(product));
        when(optionRepository.save(any(Option.class))).thenReturn(option);

        Option updatedOption = optionService.updateOption(optionRequest);

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