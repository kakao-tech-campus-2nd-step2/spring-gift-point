package gift.dto.wish;

public class AddWIshResponse {
    private final Long id;
    private final Long productId;

    public AddWIshResponse(Long id, Long productId) {
        this.id = id;
        this.productId = productId;
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }
}
