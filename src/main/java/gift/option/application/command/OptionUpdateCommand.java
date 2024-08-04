package gift.option.application.command;

public record OptionUpdateCommand(
        Long id,
        String name,
        Integer quantity
) {
}
