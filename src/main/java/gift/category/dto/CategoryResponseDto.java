package gift.category.dto;

import gift.category.entity.Category;

public record CategoryResponseDto(
    Long id,
    String name,
    String color,
    String imageUrl,
    String description) {

  public static CategoryResponseDto toDto(Category category) {
    return new CategoryResponseDto(
        category.getId(), category.getName(), category.getColor(), category.getImageUrl(),
        category.getDescription()
    );
  }
}