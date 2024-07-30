package gift.product.domain;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;

@Embeddable
public class ImageUrl {

    @Column(name = "image_url")
    private String imageUrlValue;

    public ImageUrl() {
    }

    public ImageUrl(String imageUrlValue) {
        this.imageUrlValue = imageUrlValue;
    }

    public String getImageUrlValue() {
        return imageUrlValue;
    }

    @Override
    @JsonValue
    public String toString() {
        return imageUrlValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImageUrl that = (ImageUrl) o;
        return Objects.equals(imageUrlValue, that.imageUrlValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(imageUrlValue);
    }
}