package gift.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import gift.entity.Option;
import gift.entity.Product;
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
    private ProductService productService;

    @InjectMocks
    private OptionService optionService;


    private final Long fakeOptionId = 1L;

    Option option;
    Option option1;
    Product product;

    @BeforeEach
    void setUp() {
        product = mock(Product.class);
        option = new Option("option", 1, product);
        option1 = new Option("option1", 2, product);

    }

    @Test
    void addOptionTest() {
        //given
        when(optionRepository.save(option)).thenReturn(option);

        //when
        optionService.addOption(option);

        //then
        verify(optionRepository).save(option);
    }

    @Test
    void updateOptionTest() {
        //given
        when(optionRepository.findById(fakeOptionId)).thenReturn(Optional.of(option));

        //when
        optionService.updateOption(option1, fakeOptionId);

        //then
        verify(optionRepository).save(option);
        assertAll(
            () -> assertThat(option.getName()).isEqualTo("option1"),
            () -> assertThat(option.getQuantity()).isEqualTo(2)
        );
    }

    @Test
    void deleteOptionTest() {
        //given
        when(optionRepository.findById(fakeOptionId)).thenReturn(Optional.of(option));

        //when
        optionService.deleteOption(fakeOptionId);

        //then
        verify(optionRepository).deleteById(fakeOptionId);
    }


    @Test
    void getOptionByIdTest() {
        //given
        when(optionRepository.findById(fakeOptionId)).thenReturn(Optional.of(option));

        //when
        Option option = optionService.getOptionById(fakeOptionId);

        //then
        assertAll(
            () -> assertThat(option.getName()).isEqualTo("option"),
            () -> assertThat(option.getQuantity()).isEqualTo(1)
        );

    }

    @Test
    void getOptionByProductIdTest() {
        //given
        when(optionRepository.getOptionByProductId(product.getId())).thenReturn(
            Arrays.asList(option, option1));

        //when
        List<Option> options = optionService.getOptionByProductId(product.getId());

        //then
        assertThat(options).containsExactly(option, option1);
    }

    @Test
    void testSubtractOption() {
        //given
        when(optionRepository.findById(fakeOptionId)).thenReturn(Optional.of(option1));

        //when
        optionService.subtractOption(fakeOptionId);

        //then
        verify(optionRepository).saveAndFlush(option1);
        assertThat(option1.getQuantity()).isEqualTo(1);


    }
}