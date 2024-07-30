package gift.dto.category;

import gift.model.category.Category;

import java.util.List;

public class CategoryResponse {
    public record Info(
            Long id,
            String name,
            String imageUrl,
            String description,
            String color
    ) {
        public static CategoryResponse.Info fromEntity(Category category) {
            return new CategoryResponse.Info(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
        }
    }

    public record InfoList(
            List<CategoryResponse.Info> categories
    ) {
        public static InfoList fromEntity(List<Category> categories) {
            return new InfoList(categories.stream().map(CategoryResponse.Info::fromEntity).toList());
        }

    }

}
