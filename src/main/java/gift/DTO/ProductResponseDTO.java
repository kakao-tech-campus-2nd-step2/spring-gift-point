package gift.DTO;

import java.beans.ConstructorProperties;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private Long categoryId;
    protected ProductResponseDTO(){

    }

    @ConstructorProperties({"id","name","price","imageUrl","category","options"})
    public ProductResponseDTO(Long id, String name, int price, String imageUrl,Long categoryId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
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
}
