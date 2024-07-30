package gift.wish.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.product.exception.ProductValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ProductCount {
    @Column(name = "product_count")
    private Long productCountValue;

    public ProductCount() {
    }

    public ProductCount(Long productCountValue) {
        if (productCountValue < 0) {
            throw new ProductValidException(ErrorCode.PRODUCT_COUNT_OUT_OF_RANGE_ERROR);
        }
        this.productCountValue = productCountValue;
    }

    public Long getProductCountValue() {
        return productCountValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return Long.toString(productCountValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCount that = (ProductCount) o;
        return Objects.equals(productCountValue, that.productCountValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productCountValue);
    }

}
