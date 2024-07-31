package gift.dto;

import gift.model.Product;

public class ProductResponseDTO {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private String categoryName;

    public ProductResponseDTO(Long id, String name, int price, String imageUrl, String categoryName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
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

    public String getCategoryName() {
        return categoryName;
    }

    public static ProductResponseDTO fromEntity(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getName()
        );
    }
}
