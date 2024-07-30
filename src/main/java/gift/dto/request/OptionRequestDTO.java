package gift.dto.request;

public record OptionRequestDTO(
        Long productId,
        String name,
        int quantity
) {
}
