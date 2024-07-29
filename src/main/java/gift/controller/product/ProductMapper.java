package gift.controller.product;

import gift.domain.Category;
import gift.domain.Product;
import java.util.UUID;

public class ProductMapper {

    public static Product from(ProductRequest productRequest, Category category) {
        return new Product(productRequest.name(), productRequest.price(),
            productRequest.imageUrl(), category);
    }

    public static ProductResponse toProductResponse(Product product) {
        return new ProductResponse(product.getId(), product.getName(), product.getPrice(),
            product.getImageUrl(), product.getCategoryID());
    }
}