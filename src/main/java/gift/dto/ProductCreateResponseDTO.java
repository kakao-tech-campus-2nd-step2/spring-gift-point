package gift.dto;

import gift.model.Product;

public class ProductCreateResponseDTO {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private String category;

    public ProductCreateResponseDTO(Long id, String name, int price, String imageUrl, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
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

    public String getCategory() {
        return category;
    }

    public static ProductCreateResponseDTO fromEntity(Product product) {
        return new ProductCreateResponseDTO(
            product.getId(),
            product.getName(),
            product.getPrice(),
            product.getImageUrl(),
            product.getCategory().getName()
        );
    }
}
