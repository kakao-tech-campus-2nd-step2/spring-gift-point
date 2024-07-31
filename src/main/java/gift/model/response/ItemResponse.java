package gift.model.response;

public class ItemResponse {

    private final Long productId;
    private final String name;
    private final Long price;
    private final String imageUrl;
    private final boolean isWish;

    public ItemResponse(Long productId, String name, Long price, String imageUrl, boolean isWish) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isWish = isWish;
    }

    public ItemResponse(Long productId, String name, Long price, String imageUrl) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isWish = false;
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public Long getPrice() {
        return price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean isWish() {
        return isWish;
    }
}
