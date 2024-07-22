package gift.product.service.command;

import gift.product.domain.Product;
import gift.product.domain.ProductOption;

public record ProductOptionCommand(
        String name,
        Integer quantity
) {
    public ProductOption toEntity(Product product) {
        return new ProductOption(name, quantity, product);
    }
}
