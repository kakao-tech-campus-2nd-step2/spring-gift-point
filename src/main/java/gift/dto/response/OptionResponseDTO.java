package gift.dto.response;

public record OptionResponseDTO(
        Long Id,
        String name,
        int quantity,
        Long productId
        ) {
}
