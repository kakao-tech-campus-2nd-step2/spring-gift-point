package gift.product.dto;

import gift.product.domain.ImageUrl;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;
import gift.product.validation.ValidProductName;

import java.util.Objects;

public record ProductRequestDto(@ValidProductName ProductName name, ProductPrice price, ImageUrl imageUrl, Long categoryId) {
    public ProductServiceDto toProductServiceDto() {
        return new ProductServiceDto(null, this.name, this.price, this.imageUrl, this.categoryId);
    }

    public ProductServiceDto toProductServiceDto(Long id) {
        return new ProductServiceDto(id, this.name, this.price, this.imageUrl, this.categoryId);
    }
}
