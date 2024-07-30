package gift.category.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Pattern;

@Embeddable
public class CategoryName {
    private static final int MAX_LENGTH = 15;
    private static final Pattern PATTERN = Pattern.compile("^[a-zA-Z0-9ㄱ-ㅎㅏ-ㅣ가-힣 ()\\[\\]+\\-&/_]*$");

    @Column(name = "name")
    private String productNameValue;

    public CategoryName() {
    }

    public CategoryName(String productNameValue) {
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
        CategoryName that = (CategoryName) o;
        return Objects.equals(productNameValue, that.productNameValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productNameValue);
    }
}