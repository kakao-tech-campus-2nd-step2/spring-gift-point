package gift.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import gift.option.domain.Option;
import gift.product.domain.ImageUrl;
import gift.product.domain.ProductName;
import gift.product.domain.ProductPrice;
import gift.product.validation.ValidProductName;

import java.util.List;
import java.util.Objects;

public record ProductRequestDto(@ValidProductName ProductName name, ProductPrice price, @JsonProperty("image_url") ImageUrl imageUrl,
                                @JsonProperty("category_id") Long categoryId, @JsonProperty("options") List<Option> options) {
    public ProductServiceDto toProductServiceDto() {
        return new ProductServiceDto(null, this.name, this.price, this.imageUrl, this.categoryId, this.options);
    }

    public ProductServiceDto toProductServiceDto(Long id) {
        return new ProductServiceDto(id, this.name, this.price, this.imageUrl, this.categoryId, this.options);
    }
}
