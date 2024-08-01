package gift.dto;

public record ProductUpdateResponseDTO(
    Long id,
    String name,
    String price,
    String imageUrl,
    Long categoryId
) {

}
