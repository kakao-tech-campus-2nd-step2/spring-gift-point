package gift.product.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.product.exception.ProductValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ProductPrice {

    @Column(name = "price")
    private Long productPriceValue;

    public ProductPrice() {
    }

    public ProductPrice(Long productPriceValue) {
        if (productPriceValue < 0) {
            throw new ProductValidException(ErrorCode.PRODUCT_PRICE_OUT_OF_RANGE_ERROR);
        }
        this.productPriceValue = productPriceValue;
    }

    public Long getProductPriceValue() {
        return productPriceValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return Long.toString(productPriceValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductPrice that = (ProductPrice) o;
        return Objects.equals(productPriceValue, that.productPriceValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productPriceValue);
    }
}
