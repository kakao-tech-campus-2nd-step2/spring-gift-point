package gift.dto;

import gift.model.Product;

public class ProductResponseDTO {
    private String name;
    private int price;
    private String imageUrl;
    private String categoryName;

    public ProductResponseDTO(Product product) {
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryName = product.getCategory().getName();
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

    public static ProductResponseDTO fromEntity(Product product) {
        return new ProductResponseDTO(product);
    }

}
