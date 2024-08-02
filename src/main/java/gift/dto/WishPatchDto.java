package gift.dto;

public class WishPatchDto {
    private int quantity;

    public WishPatchDto(){}
    public WishPatchDto(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
