package gift.ServiceTest;

import gift.model.Option;
import gift.repository.OptionRepository;
import gift.service.OptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void decreaseOptionQuantity() {
        Long optionId = 1L;
        Option option = new Option(optionId, "Test Option", 10);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));
        when(optionRepository.save(any(Option.class))).thenAnswer(invocation -> invocation.getArgument(0));
        Option updatedOption = optionService.decreaseOptionQuantity(1L, 3);

        assertNotNull(updatedOption);
        assertEquals(7, updatedOption.getQuantity());
        verify(optionRepository, times(1)).findById(optionId);
        verify(optionRepository, times(1)).save(updatedOption);
    }

    @Test
    void decreaseTooManyQuantity() {
        Long optionId = 1L;
        Option option = new Option(optionId, "Test Option", 2);

        when(optionRepository.findById(optionId)).thenReturn(Optional.of(option));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            optionService.decreaseOptionQuantity(optionId, 3);
        });
        assertEquals("옵션의 수량은 최소 1개 이상이어야 합니다.", exception.getMessage());
        verify(optionRepository, times(1)).findById(optionId);
        verify(optionRepository, times(0)).save(any(Option.class));
    }

    @Test
    void decreaseNotFoundOptionQuantity() {
        Long optionId = 1L;

        when(optionRepository.findById(optionId)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            optionService.decreaseOptionQuantity(optionId, 3);
        });
        assertEquals("존재하지 않는 옵션입니다.", exception.getMessage());
        verify(optionRepository, times(1)).findById(optionId);
        verify(optionRepository, times(0)).save(any(Option.class));
    }
}
