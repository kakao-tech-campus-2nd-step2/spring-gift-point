package gift.dto;

public record ProductUpdateRequestDTO(
    String name,
    String price,
    String imageUrl,
    Long categoryId
) {

}
