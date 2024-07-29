package gift.order;

import java.time.LocalDateTime;

public class OrderRequest {
    private Long optionId;
    private int quantity;
    private String message;

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getMessage() {
        return message;
    }

    public OrderInfo toEntity(){
        return new OrderInfo(optionId ,this.quantity, LocalDateTime.now(), message);
    }

}
