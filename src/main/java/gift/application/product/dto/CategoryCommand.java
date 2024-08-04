package gift.application.product.dto;

import gift.model.product.Category;

public class CategoryCommand {

    public record Register(
        String name,
        String color,
        String description,
        String imageUrl
    ) {

        public Category toEntity() {
            return new Category(name, color, description, imageUrl);
        }
    }

    public record Update(
        String name,
        String color,
        String description,
        String imageUrl
    ) {

    }

}
