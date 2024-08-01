package gift.service;

import gift.model.Option;
import gift.model.Product;
import gift.repository.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSubtractOptionQuantitySuccess() {
        Long optionId = 1L;
        int initialQuantity = 100;
        int subtractAmount = 10;

        Option option = new Option("Option Name", initialQuantity, null);
        option.assignProduct(mock(Product.class));

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        optionService.subtractOptionQuantity(optionId, subtractAmount);

        assertEquals(initialQuantity - subtractAmount, option.getQuantity());
        verify(optionRepository, times(1)).save(option);
    }

    @Test
    public void testSubtractOptionQuantityInvalidAmount() {
        Long optionId = 1L;
        int initialQuantity = 100;
        int subtractAmount = 110;

        Option option = new Option("Option Name", initialQuantity, null);
        option.assignProduct(mock(Product.class));

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        assertThrows(IllegalArgumentException.class, () -> {
            optionService.subtractOptionQuantity(optionId, subtractAmount);
        });

        verify(optionRepository, times(0)).save(option);
    }
}
