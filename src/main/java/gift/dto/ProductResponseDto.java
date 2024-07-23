package gift.dto;

import gift.domain.Product;

public class ProductResponseDto {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private String categoryName;

    public ProductResponseDto(Long id, String name, int price, String imageUrl, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
    }

    public ProductResponseDto(String name, int price, String imageUrl, String categoryName) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.categoryName = categoryName;
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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public static ProductResponseDto convertToDto(Product product) {
        return new ProductResponseDto(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getName()
        );
    }
}
