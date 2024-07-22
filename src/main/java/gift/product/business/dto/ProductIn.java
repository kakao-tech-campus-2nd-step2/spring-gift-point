package gift.product.business.dto;

import gift.product.persistence.entity.Category;
import gift.product.persistence.entity.Product;
import java.util.List;

public class ProductIn {

    public record Create(
        String name,
        String description,
        Integer price,
        String url,
        Long categoryId,
        List<OptionIn.Create> options
    ) {
        public Product toProduct(Category category) {
            return new Product(
                name,
                description,
                price,
                url,
                category
            );
        }
    }

    public record Update(
        String name,
        Integer price,
        String description,
        String imageUrl,
        Long categoryId
    ) {
    }

}
