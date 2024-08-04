package gift.product.presentation.response;

import gift.category.application.CategoryServiceResponse;

public record ProductReadCategoryResponse(
        Long id,
        String name
) {
    public static ProductReadCategoryResponse from(CategoryServiceResponse categoryServiceResponse) {
        return new ProductReadCategoryResponse(
                categoryServiceResponse.id(),
                categoryServiceResponse.name()
        );
    }
}
