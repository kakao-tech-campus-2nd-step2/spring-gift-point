package gift.product.service.dto;

import gift.product.domain.Category;

public record CategoryInfo(
        Long id,
        String name,
        String color,
        String imgUrl,
        String description
) {
    public static CategoryInfo from(Category category) {
        return new CategoryInfo(
                category.getId(),
                category.getName(),
                category.getColor(),
                category.getImgUrl(),
                category.getDescription()
        );
    }
}
