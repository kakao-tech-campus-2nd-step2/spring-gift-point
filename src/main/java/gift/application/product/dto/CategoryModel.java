package gift.application.product.dto;

import gift.model.product.Category;

public class CategoryModel {

    public record Info(
        Long id,
        String name,
        String imageUrl,
        String description,
        String color
    ) {

        public static Info from(Category category) {
            return new Info(category.getId(), category.getName(), category.getImageUrl(),
                category.getDescription(), category.getColor());
        }
    }
}
