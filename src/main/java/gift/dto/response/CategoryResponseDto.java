package gift.dto.response;

import gift.domain.Category;

public record CategoryResponseDto(Long id,
                                  String name,
                                  String color,
                                  String imageUrl,
                                  String description)
{
    public static CategoryResponseDto of(Long id, String name, String color, String imageUrl, String description) {
        return new CategoryResponseDto(id, name, color, imageUrl, description);
    }

    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
    }

}