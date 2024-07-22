package gift.category.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class CategoryDescription {

    @Column(name = "description")
    private String categoryDescriptionValue;

    public CategoryDescription() {
    }

    public CategoryDescription(String categoryDescriptionValue) {
        this.categoryDescriptionValue = categoryDescriptionValue;
    }

    public String getCategoryDescriptionValue() {
        return categoryDescriptionValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return categoryDescriptionValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryDescription that = (CategoryDescription) o;
        return Objects.equals(categoryDescriptionValue, that.categoryDescriptionValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryDescriptionValue);
    }
}
