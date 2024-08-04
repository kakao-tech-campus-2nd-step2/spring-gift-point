package gift.order.controller.request;


import gift.order.application.command.OrderCreateCommand;

public record OrderCreateRequest(
        Long optionId,
        Integer quantity,
        String message
) {
    public OrderCreateCommand toCommand() {
        return new OrderCreateCommand(
                optionId,
                quantity,
                message
        );
    }
}
