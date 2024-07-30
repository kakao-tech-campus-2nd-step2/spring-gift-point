package gift;

import gift.domain.Option;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class OptionServiceTest {

    private OptionRepository optionRepository;
    private ProductRepository productRepository;
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        optionRepository = Mockito.mock(OptionRepository.class);
        productRepository = Mockito.mock(ProductRepository.class);
        optionService = new OptionService(optionRepository, productRepository);
    }

    @Test
    void testSubtractOptionQuantity_success() {
        Option option = new Option.OptionBuilder()
            .id(1L)
            .name("Option 1")
            .quantity(10)
            .build();

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        optionService.subtractOptionQuantity(1L, 5);

        verify(optionRepository, times(1)).save(option);
    }

    @Test
    void testSubtractOptionQuantity_insufficientQuantity() {
        Option option = new Option.OptionBuilder()
            .id(1L)
            .name("Option 1")
            .quantity(10)
            .build();

        when(optionRepository.findById(1L)).thenReturn(Optional.of(option));

        assertThrows(IllegalArgumentException.class, () -> optionService.subtractOptionQuantity(1L, 15));
    }

    @Test
    void testSubtractOptionQuantity_optionNotFound() {
        when(optionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> optionService.subtractOptionQuantity(1L, 5));
    }
}
