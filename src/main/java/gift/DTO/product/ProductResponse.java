package gift.DTO.product;

import gift.domain.Product;

public class ProductResponse {

    private Long id;

    private String name;

    private int price;

    private String imageUrl;

    private Long categoryId;

    public ProductResponse() {
    }

    public ProductResponse(Long id, String name, int price, String imageUrl, Long categoryId) {
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

    public static ProductResponse fromEntity(Product productEntity) {
        return new ProductResponse(
            productEntity.getId(),
            productEntity.getName(),
            productEntity.getPrice(),
            productEntity.getImageUrl(),
            productEntity.getCategory().getId()
        );
    }
}
