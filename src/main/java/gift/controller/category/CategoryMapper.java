package gift.controller.category;

import gift.domain.Category;

public class CategoryMapper {

    public static CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(category.getId(), category.getName(), category.getColor(),
            category.getDescription(), category.getImage_url());
    }

    public static Category from(CategoryRequest categoryRequest) {
        return new Category(categoryRequest.name(), categoryRequest.color(),
            categoryRequest.description(), categoryRequest.imageUrl());
    }
}
