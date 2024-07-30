package gift.dto;

public class OrderRequest {
    private Long option_id;
    private int quantity;
    private String message;

    public Long getOptionId() {
        return option_id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }
}
