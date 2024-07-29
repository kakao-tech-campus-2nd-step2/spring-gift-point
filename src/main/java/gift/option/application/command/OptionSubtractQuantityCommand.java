package gift.option.application.command;

public record OptionSubtractQuantityCommand(
        Long id,
        Integer quantity
) {
}
