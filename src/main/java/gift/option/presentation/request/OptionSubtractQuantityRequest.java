package gift.option.presentation.request;

import gift.option.application.command.OptionSubtractQuantityCommand;
import jakarta.validation.constraints.NotNull;

public record OptionSubtractQuantityRequest(
        @NotNull(message = "옵션 수량은 필수 항목입니다.")
        Integer quantity
) {
    public OptionSubtractQuantityCommand toCommand(Long id) {
        return new OptionSubtractQuantityCommand(
                id,
                quantity
        );
    }
}
