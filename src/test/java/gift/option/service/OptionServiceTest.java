package gift.option.service;

import gift.category.domain.Category;
import gift.option.domain.Option;
import gift.option.domain.OptionCount;
import gift.option.domain.OptionName;
import gift.option.dto.OptionListResponseDto;
import gift.option.dto.OptionResponseDto;
import gift.option.dto.OptionServiceDto;
import gift.option.exception.OptionNotEnoughException;
import gift.option.exception.OptionNotFoundException;
import gift.option.repository.OptionRepository;
import gift.product.domain.ImageUrl;
import gift.product.domain.Product;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;
import gift.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private OptionService optionService;

    private Option option;
    private OptionServiceDto optionServiceDto;
    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product(1L, new ProductName("name"), new ProductPrice(10L), new ImageUrl("imageUrl"), new Category());
        option = new Option(1L, new OptionName("Option Name"), new OptionCount(10L), product);
        optionServiceDto = new OptionServiceDto(1L, new OptionName("Option Name"), new OptionCount(10L), 1L);
    }

    @Test
    void testGetAllOptions() {
        // given
        given(optionRepository.findAll()).willReturn(List.of(option));

        // when
        OptionListResponseDto options = optionService.getAllOptions();

        // then
        assertThat(options.optionResponseDtos()).hasSize(1);
        assertThat(options.optionResponseDtos().get(0).name().getOptionNameValue()).isEqualTo("Option Name");
        verify(optionRepository, times(1)).findAll();
    }

    @Test
    void testGetOptionsByProductId() {
        // given
        given(optionRepository.findByProductId(anyLong())).willReturn(List.of(option));

        // when
        OptionListResponseDto options = optionService.getOptionsByProductId(1L);

        // then
        assertThat(options.optionResponseDtos()).hasSize(1);
        assertThat(options.optionResponseDtos().get(0).name().getOptionNameValue()).isEqualTo("Option Name");
        verify(optionRepository, times(1)).findByProductId(anyLong());
    }

    @Test
    void testGetOptionById() {
        // given
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));

        // when
        OptionResponseDto foundOption = optionService.getOptionById(option.getId());

        // then
        assertThat(foundOption.name().getOptionNameValue()).isEqualTo("Option Name");
        verify(optionRepository, times(1)).findById(anyLong());
    }

    @Test
    void testGetOptionById_NotFound() {
        // given
        given(optionRepository.findById(anyLong())).willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() -> optionService.getOptionById(1L))
                .isInstanceOf(OptionNotFoundException.class);

        verify(optionRepository, times(1)).findById(anyLong());
    }

    @Test
    void testCreateOption() {
        // given
        given(productService.getProductById(anyLong())).willReturn(product);
        given(optionRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Option createdOption = optionService.createOption(optionServiceDto);

        // then
        assertThat(createdOption.getName().getOptionNameValue()).isEqualTo("Option Name");
        verify(productService, times(1)).getProductById(anyLong());
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    void testUpdateOption() {
        // given
        given(optionRepository.existsById(anyLong())).willReturn(true);
        given(productService.getProductById(anyLong())).willReturn(product);
        given(optionRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        Option updatedOption = optionService.updateOption(optionServiceDto);

        // then
        assertThat(updatedOption.getName().getOptionNameValue()).isEqualTo("Option Name");
        verify(optionRepository, times(1)).existsById(anyLong());
        verify(productService, times(1)).getProductById(anyLong());
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    void testUpdateOption_NotFound() {
        // given
        given(optionRepository.existsById(anyLong())).willReturn(false);

        // when / then
        assertThatThrownBy(() -> optionService.updateOption(optionServiceDto))
                .isInstanceOf(OptionNotFoundException.class);

        verify(optionRepository, times(1)).existsById(anyLong());
        verify(optionRepository, times(0)).save(any(Option.class));
        verify(productService, times(0)).getProductById(anyLong());
    }

    @Test
    void testDeleteOption() {
        // given
        given(optionRepository.existsById(anyLong())).willReturn(true);

        // when
        optionService.deleteOption(1L);

        // then
        verify(optionRepository, times(1)).existsById(anyLong());
        verify(optionRepository, times(1)).deleteById(anyLong());
    }

    @Test
    void testDeleteOption_NotFound() {
        // given
        given(optionRepository.existsById(anyLong())).willReturn(false);

        // when / then
        assertThatThrownBy(() -> optionService.deleteOption(1L))
                .isInstanceOf(OptionNotFoundException.class);

        verify(optionRepository, times(1)).existsById(anyLong());
        verify(optionRepository, times(0)).deleteById(anyLong());
    }

    @Test
    void testOrderOption() {
        // given
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));
        given(optionRepository.save(any(Option.class))).willAnswer(invocation -> invocation.getArgument(0));

        // when
        optionService.orderOption(1L, 5);

        // then
        assertThat(option.getCount().getOptionCountValue()).isEqualTo(5L);
        verify(optionRepository, times(1)).findById(anyLong());
        verify(optionRepository, times(1)).save(any(Option.class));
    }

    @Test
    void testOrderOption_NotEnoughStock() {
        // given
        given(optionRepository.findById(anyLong())).willReturn(Optional.of(option));

        // when / then
        assertThatThrownBy(() -> optionService.orderOption(1L, 15))
                .isInstanceOf(OptionNotEnoughException.class);

        verify(optionRepository, times(1)).findById(anyLong());
        verify(optionRepository, times(0)).save(any(Option.class));
    }
}
