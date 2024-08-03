package gift.controller.category.dto;

import gift.model.Category;
import java.util.List;

public class CategoryResponse {

    public record Info(
        Long id,
        String name,
        String imageUrl,
        String description,
        String color
    ) {
        public static Info from(Category category) {
            return new Info(
                category.getId(),
                category.getName(),
                category.getImageUrl(),
                category.getDescription(),
                category.getColor()
            );
        }
    }

    public record InfoList(
        List<Info> categories
    ) {
        public static InfoList from(List<Category> categories) {
            return new InfoList(
                categories.stream()
                    .map(Info::from)
                    .toList()
            );
        }
    }
}
