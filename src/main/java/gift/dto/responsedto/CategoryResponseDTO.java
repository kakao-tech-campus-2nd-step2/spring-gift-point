package gift.dto.responsedto;

import gift.domain.Category;

public record CategoryResponseDTO(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description) {

    public static CategoryResponseDTO from(Category category) {
        return new CategoryResponseDTO(category.getId(), category.getName(), category.getColor(),
            category.getImageUrl(), category.getDescription());
    }
}
