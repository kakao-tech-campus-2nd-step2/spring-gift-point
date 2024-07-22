package gift.product.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import gift.global.response.ErrorCode;
import gift.product.exception.ProductValidException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class ProductName {
    private static final int MAX_LENGTH = 15;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-&/_]*$");

    @Column(name = "name")
    private String productNameValue;

    public ProductName() {
    }

    public ProductName(String productNameValue) {
        if (Objects.isNull(productNameValue)) {
            throw new ProductValidException(ErrorCode.PRODUCT_NAME_LENGTH_ERROR);
        }

        productNameValue = productNameValue.trim();

        if (productNameValue.isEmpty() || productNameValue.length() > MAX_LENGTH) {
            throw new ProductValidException(ErrorCode.PRODUCT_NAME_LENGTH_ERROR);
        }

        if (!PATTERN.matcher(productNameValue).matches()) {
            throw new ProductValidException(ErrorCode.PRODUCT_NAME_PATTER_ERROR);
        }
        this.productNameValue = productNameValue;
    }

    public String getProductNameValue() {
        return productNameValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return productNameValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductName that = (ProductName) o;
        return Objects.equals(productNameValue, that.productNameValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNameValue);
    }
}