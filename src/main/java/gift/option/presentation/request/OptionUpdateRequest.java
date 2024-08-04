package gift.option.presentation.request;

import gift.option.application.command.OptionUpdateCommand;

public record OptionUpdateRequest(
        String name,
        Integer quantity
) {
    public OptionUpdateCommand toCommand(Long optionId, Long productId) {
        return new OptionUpdateCommand(
                optionId,
                name,
                quantity,
                productId
        );
    }
}
