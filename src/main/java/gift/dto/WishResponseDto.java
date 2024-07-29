package gift.dto;


public class WishResponseDto {
    private Long id;
    private Long memberId;
    private Long productId;
    private int quantity;

    public WishResponseDto() {
    }

    public WishResponseDto(Long id, Long memberId, Long productId, int quantity) {
        this.id = id;
        this.memberId = memberId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}