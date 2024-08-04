package gift.category.application.command;

import gift.category.domain.Category;

public record CategoryCreateCommand (
        String name,
        String color,
        String description,
        String imageUrl
){
    public Category toCategory() {
        return new Category(name, color, description, imageUrl);
    }
}
