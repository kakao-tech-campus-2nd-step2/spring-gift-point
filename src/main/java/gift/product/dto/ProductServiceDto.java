package gift.product.dto;

import gift.category.domain.Category;
import gift.option.domain.Option;
import gift.product.domain.ImageUrl;
import gift.product.domain.Product;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;

import java.util.List;

public record ProductServiceDto(Long id, ProductName name, ProductPrice price, ImageUrl imageUrl,
                                Long categoryId, List<Option> options) {
    public Product toProduct() {
        return new Product(id, name, price, imageUrl, null);
    }

    public Product toProduct(Category category) {
        return new Product(id, name, price, imageUrl, category);
    }
}
