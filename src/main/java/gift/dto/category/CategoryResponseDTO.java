package gift.dto.category;

public record CategoryResponseDTO(Long id,
                                  String name,
                                  String color,
                                  String imageUrl,
                                  String description) {
}
