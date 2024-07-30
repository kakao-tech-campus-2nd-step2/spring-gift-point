package gift.dto.response;

public record ProductResponseDTO(Long productId,
                                 String name,
                                 int price,
                                 String imageUrl
) {
}
