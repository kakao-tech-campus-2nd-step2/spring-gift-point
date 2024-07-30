package gift.controller.dto.response;

import gift.model.Category;

import java.util.List;

public class CategoryResponse {

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

    public record Info(
            Long id,
            String name,
            String color,
            String imageUrl,
            String description
    ) {
        public static Info from(Category category) {
            return new Info(
                    category.getId(),
                    category.getName(),
                    category.getColor(),
                    category.getImageUrl(),
                    category.getDescription()
            );
        }
    }
}
