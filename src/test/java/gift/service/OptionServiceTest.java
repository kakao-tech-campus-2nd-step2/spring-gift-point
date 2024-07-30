package gift.service;

import gift.domain.Option;
import gift.repository.OptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    private Option option;

    private static final Long ID = 1L;
    private static final Integer QUANTITY = 10;
    private static final Integer DECREASE_QUANTITY = 5;
    private static final Integer MINUS_QUANTITY = -5;
    private static final Integer BIG_QUANTITY = 20;

    @BeforeEach
    public void setUp() {
        option = new Option("Test Option", null);
        option.setId(ID);
        option.setQuantity(QUANTITY);
    }

    @Test
    public void testDecreaseQuantitySuccess() {
        // Arrange
        when(optionRepository.findById(ID)).thenReturn(Optional.of(option));

        // Act
        optionService.decreaseQuantity(ID, DECREASE_QUANTITY);

        // Assert
        verify(optionRepository).findById(ID);
        verify(optionRepository).save(option);
        assert(option.getQuantity() == QUANTITY - DECREASE_QUANTITY);
    }

    @Test
    public void testDecreaseQuantityThrowsEntityNotFoundException() {
        // Arrange
        when(optionRepository.findById(ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> optionService.decreaseQuantity(ID, DECREASE_QUANTITY));
        verify(optionRepository).findById(ID);
        verify(optionRepository, never()).save(any(Option.class));
    }

    @Test
    public void testDecreaseQuantityThrowsIllegalArgumentExceptionForNegativeAmount() {
        // Arrange
        when(optionRepository.findById(ID)).thenReturn(Optional.of(option));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> optionService.decreaseQuantity(ID, MINUS_QUANTITY));
        verify(optionRepository).findById(ID);
        verify(optionRepository, never()).save(option);
    }

    @Test
    public void testDecreaseQuantityThrowsIllegalArgumentExceptionForInsufficientQuantity() {
        // Arrange
        when(optionRepository.findById(ID)).thenReturn(Optional.of(option));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> optionService.decreaseQuantity(ID, BIG_QUANTITY));
        verify(optionRepository).findById(ID);
        verify(optionRepository, never()).save(option);
    }
}
