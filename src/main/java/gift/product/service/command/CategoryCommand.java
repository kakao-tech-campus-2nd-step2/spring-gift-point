package gift.product.service.command;

import gift.product.domain.Category;

public record CategoryCommand(
        String name,
        String color,
        String imgUrl,
        String description
) {
    public Category toEntity() {
        return new Category(name(), color(), imgUrl(), description());
    }
}
