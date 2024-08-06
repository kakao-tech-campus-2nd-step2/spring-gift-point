package gift.domain.option;

public record OptionResponse(
    Long id,
    String name,
    Long quantity
) {

    public OptionResponse(Option option) {
        this(option.getId(), option.getName(), option.getQuantity());
    }
}
