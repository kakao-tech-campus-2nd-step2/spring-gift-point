package gift.product.model.dto;

public class ProductResponse {
    private final Long id;
    private final String name;
    private final int price;
    private final String imageUrl;
    private final Long categoryId;
    private final Long wishCount;

    public ProductResponse(Product product, Long wishCount) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
        this.categoryId = product.getCategory().getId();
        this.wishCount = wishCount;
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

    public Long getWishCount() {
        return wishCount;
    }
}
