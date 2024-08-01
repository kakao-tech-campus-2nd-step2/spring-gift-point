package gift.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductRequest {
    @NotNull
    private Long id;

    @NotNull
    @Size(min = 1, max = 15)
    private String name;

    @NotNull
    private int price;

    @NotNull
    private String imageUrl;

    @NotNull
    private Long categoryId;

    public ProductRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategory() {
        return categoryId;
    }

    public void setCategory(Long categoryId) {
        this.categoryId = categoryId;
    }
}
