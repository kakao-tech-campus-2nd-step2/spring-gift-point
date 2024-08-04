package gift.dto;

import gift.annotation.ProductName;
import gift.entity.Category;
import gift.entity.Product;

public class ProductUpdateRequest {

    @ProductName
    private String name;
    private int price;
    private String imageUrl;
    private Long categoryId;

    public ProductUpdateRequest(String name, int price, String imageUrl, Long categoryId) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryId = categoryId;
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

    public Product toEntity(Category category) {
        return new Product(name, price, imageUrl, category);
    }
}
