package gift.product.application.dto.response;

import gift.product.service.dto.CategoryInfo;

public record CategoryResponse(
        Long id,
        String name,
        String color,
        String imgUrl,
        String description
) {
    public static CategoryResponse from(CategoryInfo categoryInfo) {
        return new CategoryResponse(
                categoryInfo.id(),
                categoryInfo.name(),
                categoryInfo.color(),
                categoryInfo.imgUrl(),
                categoryInfo.description()
        );
    }
}
