package gift.product.service.command;

import gift.product.domain.Category;
import gift.product.domain.Product;

public record ProductCommand(
        String name,
        Integer price,
        String imgUrl,
        String categoryName
) {
    public Product toEntity(Category category) {
        return new Product(name, price, imgUrl, category);
    }
}
