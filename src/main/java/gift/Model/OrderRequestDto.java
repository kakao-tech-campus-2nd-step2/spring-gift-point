package gift.Model;

public class OrderRequestDto {
    long productId;
    long optionId;
    int quantity;
    long memberId;

    public OrderRequestDto() {
    }

    public OrderRequestDto(long productId, long optionId, int quantity, long memberId) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.memberId = memberId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getOptionId() {
        return optionId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

}
