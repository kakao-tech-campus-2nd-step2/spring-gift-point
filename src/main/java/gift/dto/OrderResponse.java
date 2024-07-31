package gift.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import java.time.LocalDateTime;

@JsonPropertyOrder({"id", "option_id", "quantity", "order_date_time", "message"})
public class OrderResponse {

    private Long id;

    @JsonProperty("option_id")
    private Long optionId;
    private int quantity;

    @JsonProperty("order_date_time")
    private LocalDateTime orderDateTime;
    private String message;

    public OrderResponse(Long id, Long optionId, int quantity, LocalDateTime orderDateTime,
        String message) {
        this.id = id;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public Long getOptionId() {
        return optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public String getMessage() {
        return message;
    }

}
