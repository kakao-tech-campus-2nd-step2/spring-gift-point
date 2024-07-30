package gift.dto;

public class OrderDTO {
    private Long OptionId;
    private int quantity;
    private String message;

    public OrderDTO(Long OptionId, int quantity, String message) {
        this.OptionId = OptionId;
        this.quantity = quantity;
        this.message = message;
    }

    public Long getOptionId() {
        return OptionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
