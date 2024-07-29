package gift.service;

import gift.domain.Option;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;


@ExtendWith(MockitoExtension.class)
class OptionServiceTest {
    @InjectMocks
    OptionService optionService;

    @Mock
    OptionRepository optionRepository;

    private Option createOption(String name, int quantity) {
        return new Option(name, quantity);
    }

    /*
    @Test
    @DisplayName("옵션 수량 감소 정상")
    void 옵션_수량_감소_정상_테스트() {
        // given
        Long optionId = 1L;
        Option option = createOption("옵션1", 10);

        given(optionRepository.findById(optionId)).willReturn(Optional.of(option));
        OrderRequest order = new OrderRequest(optionId, 5, "hi");
        // when
        optionService.subtractQuantityById(order);

        // then
        assertAll(
                () -> assertThat(option.getQuantity()).isEqualTo(5)
        );
    }

    @Test
    @DisplayName("옵션 수량 초과 감소 시 예외")
    void 옵션_수량_초과_감소_예외_테스트() {
        // given
        Long optionId = 1L;
        Option option = createOption("옵션1", 10);

        given(optionRepository.findById(optionId)).willReturn(Optional.of(option));
        OrderRequest order = new OrderRequest(optionId, 11, "hi");

        // expected
        assertThatThrownBy(() -> optionService.subtractQuantityById(order))
                .isInstanceOf(InsufficientQuantityException.class)
                .hasMessage(Messages.INSUFFICIENT_QUANTITY);
    } */
}