package gift.product.model.dto;

public class ProductResponse {
    private Long id;
    private String name;
    private int price;
    private String imageUrl;
    private Long wishCount;

    public ProductResponse(Product product, Long wishCount) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageUrl();
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

    public Long getWishCount() {
        return wishCount;
    }
}
