package gift.option;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import gift.entity.OptionEntity;
import gift.repository.OptionRepository;
import gift.service.OptionService;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @InjectMocks
    private OptionService optionService;

    @Mock
    private OptionRepository optionRepository;

    @Test
    @DisplayName("정상적인 수로 감소 시킬때")
    void checkSuccessSubQuantity() {
        //given
        OptionEntity optionEntity = new OptionEntity(1L, "Option1", 1000L);
        given(optionRepository.findById(1L)).willReturn(Optional.of(optionEntity));

        //when
        optionService.subQuantity(1L, 999L);
        //then
        Assertions.assertThat(optionEntity.getQuantity()).isEqualTo(1L);
    }

    @Test
    @DisplayName("옵션 수량 보다 더 많이 감소 시킬 때")
    void checkFailSubQuantity() {
        //given
        OptionEntity optionEntity = new OptionEntity(1L, "Option1", 1L);
        given(optionRepository.findById(1L)).willReturn(Optional.of(optionEntity));

        //when, then
        assertThatThrownBy(() -> optionService.subQuantity(1L, 999L)).isInstanceOf(IllegalArgumentException.class);
    }

}
