package gift.dto.productDTO;

public record ProductUpdateRequestDTO(
    String name,
    String price,
    String imageUrl,
    Long categoryId
) {

}