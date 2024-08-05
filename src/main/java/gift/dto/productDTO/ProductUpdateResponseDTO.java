package gift.dto.productDTO;

public record ProductUpdateResponseDTO(
    Long id,
    String name,
    String price,
    String imageUrl,
    Long categoryId
) {

}