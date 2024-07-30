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
        public static Info fromEntity(Category category) {
            return new Info(category.getId(), category.getName(), category.getColor(), category.getImageUrl(), category.getDescription());
        }
    }

    public record InfoList(
            List<Info> categories
    ) {
        public static InfoList fromEntity(List<Category> categories) {
            return new InfoList(categories.stream().map(Info::fromEntity).toList());
        }

    }

}
