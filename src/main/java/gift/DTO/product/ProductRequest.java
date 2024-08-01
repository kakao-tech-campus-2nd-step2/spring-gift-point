package gift.DTO.product;

import gift.domain.Category;
import gift.domain.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class ProductRequest {

    @NotBlank(message = "Please enter the product name")
    @Size(max = 15, message = "The product name can be up to 15 characters long (including spaces)")
    @Pattern(
        regexp = "^[a-zA-Z0-9가-힣\\s\\(\\)\\[\\]\\+\\-&/_]*$",
        message = "Only the following special characters are allowed: (), [], +, -, &, /, _"
    )
    @Pattern(regexp = "^(?!.*카카오).*$", message = "The term '카카오' can only be used after consultation with the responsible MD")
    private final String name;

    @Min(value = 0, message = "The product price must be zero or higher.")
    private final int price;

    @Pattern(
        regexp = ".*\\.(jpg|jpeg|png|gif|bmp)$",
        message = "Invalid URL format"
    )
    private final String imageUrl;

    @Min(value = 1, message = "Category id must be positive")
    private final Long categoryId;

    public ProductRequest() {
        this("name", 1000, "image url", 1L);
    }

    public ProductRequest(String name, int price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
    }

    public Product toEntity(Category category) {
        return new Product(name, price, imageUrl, category);
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
}
