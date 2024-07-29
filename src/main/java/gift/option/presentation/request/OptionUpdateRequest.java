package gift.option.presentation.request;

import gift.option.application.command.OptionUpdateCommand;

public record OptionUpdateRequest(
        Long id,
        String name,
        Integer quantity
) {
    public OptionUpdateCommand toCommand() {
        return new OptionUpdateCommand(
                id,
                name,
                quantity
        );
    }
}
