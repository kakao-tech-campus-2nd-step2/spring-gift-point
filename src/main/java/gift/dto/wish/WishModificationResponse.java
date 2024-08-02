package gift.dto.wish;

public class WishModificationResponse {
    private Long id;
    private int quantity;

    public WishModificationResponse(Long id, int quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
