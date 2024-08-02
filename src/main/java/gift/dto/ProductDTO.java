package gift.dto;

import gift.validation.ValidProductName;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import org.hibernate.validator.constraints.URL;

public class ProductDTO {

    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Long categoryId;
    private final String categoryName;

    public ProductDTO(Long id, String name, int price, String imageUrl, Long categoryId, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }
}
