package gift.dto.categoryDTO;

public record CategoryResponseDTO(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description
) {

}
