package gift.category.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CategoryColor {

    @Column(name = "color")
    private String productPriceValue;

    public CategoryColor() {
    }

    public CategoryColor(String productPriceValue) {
        this.productPriceValue = productPriceValue;
    }

    public String getProductPriceValue() {
        return productPriceValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return productPriceValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryColor that = (CategoryColor) o;
        return Objects.equals(productPriceValue, that.productPriceValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productPriceValue);
    }
}
