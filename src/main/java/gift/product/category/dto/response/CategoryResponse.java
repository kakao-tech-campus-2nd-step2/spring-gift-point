package gift.product.category.dto.response;

import gift.product.category.entity.Category;

public record CategoryResponse(
    Long id,
    String name
) {

    public static CategoryResponse from(Category category) {
        return new CategoryResponse(category.getId(), category.getName());
    }

}
