package gift.application.product.dto;

import gift.model.product.Category;
import gift.model.product.Product;

public class ProductCommand {

    public record Register(
        String name,
        Integer price,
        String imageUrl,
        Long categoryId
    ) {

        public Product toEntity(Category category) {
            return new Product(null, name, price, imageUrl, category);
        }
    }

    public record Update(
        String name,
        Integer price,
        String imageUrl,
        Long categoryId
    ) {

    }
}
