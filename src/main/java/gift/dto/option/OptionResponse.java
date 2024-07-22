package gift.dto.option;

public record OptionResponse(
    Long id,
    String name,
    int quantity,
    Long productId
) {

}
