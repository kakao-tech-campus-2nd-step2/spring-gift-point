package gift.DTO;

public class OrderRequestDTO {
    private Long productId;
    private Long optionId;
    private Long quantity;
    private Long userId;

    public Long getUserId() {
        return userId;
    }

    public Long getProductId() {
        return productId;
    }


    public Long getOptionId() {
        return optionId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

}
