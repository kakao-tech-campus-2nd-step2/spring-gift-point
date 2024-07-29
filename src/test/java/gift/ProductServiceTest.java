package gift;

import gift.domain.Option;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class ProductServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    @DisplayName("옵션 수량 차감 성공 테스트")
    void subtractOptionQuantityTest_Success() {
        Long productId = 1L;
        String optionName = "Option1";
        int quantityToSubtract = 5;

        Option option = new Option();
        option.setId(1L);
        option.setName(optionName);
        option.setQuantity(10);

        when(optionRepository.findByProductIdAndName(productId, optionName)).thenReturn(Optional.of(option));

        productService.subtractOptionQuantity(productId, optionName, quantityToSubtract);

        assertThat(option.getQuantity()).isEqualTo(5);
        verify(optionRepository, times(1)).save(option);
    }

    @Test
    @DisplayName("존재하는 옵션 수량보다 삭제하고자 하는 수량이 많은 경우 테스트")
    void subtractOptionQuantityTest_InsufficientQuantity() {
        Long productId = 1L;
        String optionName = "Option1";
        int quantityToSubtract = 15;

        Option option = new Option();
        option.setId(1L);
        option.setName(optionName);
        option.setQuantity(10);

        when(optionRepository.findByProductIdAndName(productId, optionName)).thenReturn(Optional.of(option));

        assertThatThrownBy(() -> {
            productService.subtractOptionQuantity(productId, optionName, quantityToSubtract);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("차감할 수량이 현재 수량보다 많습니다.");
    }

    @Test
    @DisplayName("수량을 차감하고자 하는 옵션이 존재하지 않는 경우")
    void subtractOptionQuantityTest_OptionNotFound() {
        Long productId = 1L;
        String optionName = "Option1";
        int quantityToSubtract = 5;

        when(optionRepository.findByProductIdAndName(productId, optionName)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            productService.subtractOptionQuantity(productId, optionName, quantityToSubtract);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 옵션이 존재하지 않습니다.");
    }
}
