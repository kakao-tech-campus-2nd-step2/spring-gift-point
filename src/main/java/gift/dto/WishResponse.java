package gift.dto;

public class WishResponse {
    private final Long id;
    private final Long productId;
    private final String productName;
    private final String productImgUrl;
    private final Long memberId;
    private final String email;

    public WishResponse(Long id, Long productId, String productName, String productImgUrl, Long userId, String email) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.productImgUrl = productImgUrl;
        this.memberId = userId;
        this.email = email;
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

    public String getProductImgUrl() {
        return productImgUrl;
    }

    public Long getMemberId() {
        return memberId;
    }

    public String getEmail() {
        return email;
    }
}