package gift.order.application.command;

import gift.option.application.command.OptionSubtractQuantityCommand;
import gift.option.domain.Option;
import gift.order.domain.Order;

public record OrderCreateCommand(
        Long optionId,
        Integer quantity,
        String message
) {
    public OptionSubtractQuantityCommand toOptionSubtractQuantityCommand() {
        return new OptionSubtractQuantityCommand(optionId, quantity);
    }

    public Order toOrder(Option option) {
        return new Order(option, quantity, message);
    }
}
