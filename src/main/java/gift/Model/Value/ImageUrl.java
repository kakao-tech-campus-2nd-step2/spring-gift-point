package gift.Model.Value;

import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ImageUrl {

    private String value;

    protected ImageUrl() {}

    public ImageUrl(String value) {
        validateImageUrl(value);

        this.value = value;
    }

    private void validateImageUrl(String value) {
        if (value == null || value.isBlank())
            throw new IllegalArgumentException("value를 입력해주세요");

    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (!(object instanceof ImageUrl))
            return false;

        ImageUrl imageUrl = (ImageUrl) object;
        return Objects.equals(this.value, imageUrl.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}
