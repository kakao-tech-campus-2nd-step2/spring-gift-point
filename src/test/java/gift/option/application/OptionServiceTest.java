package gift.option.application;

import gift.exception.type.ConcurrencyException;
import gift.exception.type.InvalidOptionQuantityException;
import gift.exception.type.NotFoundException;
import gift.option.application.command.OptionSubtractQuantityCommand;
import gift.option.domain.Option;
import gift.option.domain.OptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class OptionServiceTest {

    @Mock
    private OptionRepository optionRepository;

    @InjectMocks
    private OptionService optionService;

    private Option option;

    @BeforeEach
    void setUp() {
        option = new Option(1L, "Option1", 100);

        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 낙관적_락_예외가_발생할_때_ConcurrencyException() {
        // Given
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(1L, 100);
        when(optionRepository.findByIdWithLock(any())).thenReturn(Optional.of(option));
        doThrow(new ObjectOptimisticLockingFailureException(Option.class, option.getId()))
                .when(optionRepository).save(option);

        // When
        Throwable thrown = catchThrowable(() -> optionService.subtractOptionQuantity(command));

        // Then
        assertThat(thrown).isInstanceOf(ConcurrencyException.class)
                .hasMessageContaining("동시성 문제가 발생했습니다. 다시 시도해주세요.");
    }

    @Test
    void 옵션_수량_차감_성공() {
        // Given
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(1L, 100);
        when(optionRepository.findByIdWithLock(any())).thenReturn(Optional.of(option));

        // When
        optionService.subtractOptionQuantity(command);

        // Then
        assertThat(option.getQuantity()).isEqualTo(0);
    }

    @Test
    void 옵션이_존재하지_않을_때_NotFoundExcenption() {
        // Given
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(1L, 100);
        when(optionRepository.findByIdWithLock(any())).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> optionService.subtractOptionQuantity(command))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("해당 옵션이 존재하지 않습니다.");
    }

    @Test
    void 수량이_부족할_때_InvalidOptionQuantityException() {
        // Given
        OptionSubtractQuantityCommand command = new OptionSubtractQuantityCommand(1L, 101);
        when(optionRepository.findByIdWithLock(any())).thenReturn(Optional.of(option));

        // When & Then
        assertThatThrownBy(() -> optionService.subtractOptionQuantity(command))
                .isInstanceOf(InvalidOptionQuantityException.class)
                .hasMessageContaining("수량이 0 이하가 될 수 없습니다.");
    }
}
