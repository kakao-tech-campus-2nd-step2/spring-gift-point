package gift.response;

import gift.model.Category;

public record CategoryResponse(
    Long id,
    String name
) {

    private CategoryResponse(Category category) {
        this(category.getId(), category.getName());
    }

    public static CategoryResponse createCategoryResponse(Category category) {
        return new CategoryResponse(category);
    }
}
