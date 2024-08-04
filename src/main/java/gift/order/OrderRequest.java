package gift.order;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.time.LocalDateTime;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
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
