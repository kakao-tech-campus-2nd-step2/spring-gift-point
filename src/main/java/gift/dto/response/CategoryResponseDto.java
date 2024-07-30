package gift.dto.response;

import gift.domain.Category;

public record CategoryResponseDto(Long id, String name, String color) {

    public static CategoryResponseDto of(Long id, String name, String color) {
        return new CategoryResponseDto(id, name, color);
    }

    public static CategoryResponseDto from(Category category) {
        return new CategoryResponseDto(category.getId(), category.getName(), category.getColor());
    }

}