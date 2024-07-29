package gift.option;

public record OptionResponse(
    Long id,
    String name,
    int quantity
) {

    public OptionResponse(Option option) {
        this(option.getId(), option.getName(), option.getQuantity());
    }
}
