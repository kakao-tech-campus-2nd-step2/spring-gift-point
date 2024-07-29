package gift.category;

import gift.category.dto.CategoryReqDto;
import gift.category.entity.Category;

public class CategoryFixture {

    public static Category createCategory(String name) {
        return new Category(name, "#FFFFFF", "categoryImageUrl", "categoryDescription");
    }

    public static Category createCategory(String name, String color, String imageUrl, String description) {
        return new Category(name, color, imageUrl, description);
    }

    public static CategoryReqDto createCategoryReqDto(String name, String color, String imageUrl, String description) {
        return new CategoryReqDto(name, color, imageUrl, description);
    }

    public static CategoryReqDto createCategoryReqDto(String name) {
        return new CategoryReqDto(name, "#FFFFFF", "categoryImageUrl", "categoryDescription");
    }
}
