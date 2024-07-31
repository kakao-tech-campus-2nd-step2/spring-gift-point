package gift.dto;

public class OrderRequest {
    private Long product_id;
    private Long option_id;
    private int quantity;
    private String message;

    public Long getProduct_id() {
        return product_id;
    }

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
