package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.entity.Option;
import gift.option.repository.OptionRepository;
import gift.option.service.OptionService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @InjectMocks
    OptionService optionService;

    @Mock
    OptionRepository optionRepository;

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void insertOptionTest() {
        // given
        var optionId = 1L;
        var name = "화이트초콜릿";
        var quantity = 10000;
        var productId = 1L;

        var optionRequestDto = new OptionRequestDto(name, quantity);
        var option = new Option(optionId, name, quantity, productId);

        given(optionRepository.save(any(Option.class))).willReturn(option);

        // when, then
        Assertions.assertThatCode(() -> optionService.insertOption(optionRequestDto, productId))
            .doesNotThrowAnyException();

        then(optionRepository).should().save(any(Option.class));
    }

    /*
     * - [ ] 정상적인 경우
     */
    @Test
    public void selectOptionsTest() {
        // given
        var optionId = 1L;
        var name = "화이트초콜릿";
        var quantity = 10000;
        var productId = 1L;
        var optionId2 = 2L;
        var name2 = "다크초콜릿";
        var quantity2 = 20000;

        var option = new Option(optionId, name, quantity, productId);
        var option2 = new Option(optionId2, name2, quantity2, productId);
        var options = List.of(option, option2);

        given(optionRepository.findAll()).willReturn(options);

        // when, then
        Assertions.assertThat(optionService.selectOptions()).isEqualTo(options.stream().map(
            OptionResponseDto::fromOption).toList());

        then(optionRepository).should().findAll();
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 존재하지 않는 옵션 조회하는 경우
     */
    @Test
    public void selectOptionTest() {
        // given
        var optionId = 1L;
        var name = "화이트초콜릿";
        var quantity = 10000;
        var productId = 1L;
        var invalidOptionId = 2L;

        var option = new Option(optionId, name, quantity, productId);

        given(optionRepository.findById(optionId)).willReturn(Optional.of(option));
        given(optionRepository.findById(invalidOptionId)).willReturn(Optional.empty());

        // when, then
        // 정상적인 경우
        Assertions.assertThat(optionService.selectOption(optionId))
            .isEqualTo(OptionResponseDto.fromOption(option));
        // 존재하지 않는 경우
        Assertions.assertThatThrownBy(() -> optionService.selectOption(invalidOptionId))
            .isInstanceOf(
                NoSuchElementException.class);

        // 정상적인 경우
        then(optionRepository).should().findById(optionId);
        // 존재하지 않는 경우
        then(optionRepository).should().findById(invalidOptionId);
    }
}
