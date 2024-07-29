package gift.dto;


public class OptionQuantityDTO {

    private int quantity;

    public OptionQuantityDTO() {}

    public OptionQuantityDTO(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}