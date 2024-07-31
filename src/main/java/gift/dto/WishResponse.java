package gift.dto;

public class WishResponse {
    private Long id;
    private Long productId;
    private String productName;
    private String imageUrl;

    public WishResponse(Long id, Long productId, String productName, String imageUrl) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.imageUrl = imageUrl;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
