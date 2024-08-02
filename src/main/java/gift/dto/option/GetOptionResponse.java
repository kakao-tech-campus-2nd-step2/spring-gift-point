package gift.dto.option;

public record GetOptionResponse(
    long optionId,
    String name,
    int quantity
) {

}
