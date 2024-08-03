package gift.dto.product;

public record ProductModifyResponseDTO(Long id,
                                       String name,
                                       Long price,
                                       String imageUrl,
                                       Long categoryId) {
}
