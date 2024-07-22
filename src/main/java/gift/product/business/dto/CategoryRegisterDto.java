package gift.product.business.dto;

import gift.product.persistence.entity.Category;

public record CategoryRegisterDto(
    String name
) {

    public Category toCategory() {
        return new Category(
            name()
        );
    }
}
