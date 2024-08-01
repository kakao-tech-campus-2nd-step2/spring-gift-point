package gift.service;

import gift.domain.category.Category;
import gift.domain.option.Option;
import gift.domain.option.OptionRepository;
import gift.domain.product.Product;
import gift.domain.product.ProductRepository;
import gift.dto.OptionRequestDto;
import gift.dto.OptionResponseDto;
import gift.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OptionServiceTest {
    @Mock
    private OptionRepository optionRepository;
    @Mock
    private ProductRepository productRepository;
    @InjectMocks
    private OptionService optionService;

    @Test
    void 옵션_조회_성공() {
        //given
        Product product = new Product("더미", 10000, "test.jpg",
                new Category("테스트", "##", "설명", "test"),
                List.of(new Option("옵션1", 100), new Option("옵션2", 100)));

        given(productRepository.findById(any()))
                .willReturn(Optional.of(product));

        //when
        List<OptionResponseDto> actual = optionService.getOptions(1L);

        //then
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getName()).isEqualTo("옵션1");
        assertThat(actual.get(1).getName()).isEqualTo("옵션2");

    }

    @Test
    void 옵션_생성_성공() {
        //given
        //when
        optionService.saveOption(new OptionRequestDto("새 옵션", 10));

        //then
        verify(optionRepository).save(any());
    }

    @Test
    void 옵션_차감_성공() {
        //given
        Option option = new Option("옵션", 100);
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(option));
        //when
        optionService.subtractOption(1L, 70);
        //then
        assertThat(option.getQuantity()).isEqualTo(30);
    }

    @Test
    void 옵션_차감_실패_재고보다_많은_요청() {
        //given
        Option option = new Option("옵션", 100);
        given(optionRepository.findById(any()))
                .willReturn(Optional.of(option));
        //when
        //then
        assertThatExceptionOfType(CustomException.class)
                .isThrownBy(() -> optionService.subtractOption(1L, 110));
    }
}
