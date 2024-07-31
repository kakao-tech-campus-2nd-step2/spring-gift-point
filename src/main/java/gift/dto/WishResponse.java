package gift.dto;

public class WishResponse {
    private Long id;
    private Long productId;
    private String productName;
    private int productNumber;

    public WishResponse(Long id, Long productId, String productName, int productNumber) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productNumber = productNumber;
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

    public int getProductNumber() {
        return productNumber;
    }
}
