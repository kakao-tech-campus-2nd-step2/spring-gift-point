package gift.product.business.dto;

import gift.product.persistence.entity.Category;

public class CategoryIn {

    public record Create(
        String name,
        String description,
        String color,
        String imageUrl
    ) {

        public Category toCategory() {
            return new Category(name(), description(), color(), imageUrl());
        }
    }

    public record Update(
        Long id,
        String name,
        String description,
        String color,
        String imageUrl) {

    }
}
