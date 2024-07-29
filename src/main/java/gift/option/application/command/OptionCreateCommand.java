package gift.option.application.command;

import gift.option.domain.Option;

public record OptionCreateCommand(
        String name,
        Integer quantity
) {
    public Option toOption() {
        return new Option(name, quantity);
    }
}
